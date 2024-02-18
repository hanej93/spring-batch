package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._4_partitioning;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class PartitioningConfiguration {

	public static final int CHUNK_SIZE = 100;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;

	@Bean
	public Job batchJob() throws InterruptedException {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(masterStep())
			.listener(new StopWatchJobListener())
			.build();
	}

	@Bean
	public Step masterStep() {
		return new StepBuilder("masterStep", jobRepository)
			.partitioner(slaveStep().getName(), partitioner())
			.step(slaveStep())
			.gridSize(4)
			.taskExecutor(new SimpleAsyncTaskExecutor())
			.build();
	}

	@Bean
	public Step slaveStep() {
		return new StepBuilder("slaveStep", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(pagingItemReader(null, null))
			.writer(customItemWriter())
			.build();
	}

	@Bean
	public Partitioner partitioner() {
		ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();

		columnRangePartitioner.setTable("customer");
		columnRangePartitioner.setColumn("id");
		columnRangePartitioner.setDataSource(dataSource);

		return columnRangePartitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(4);
		taskExecutor.setMaxPoolSize(8);
		taskExecutor.setThreadNamePrefix("async-thread");

		return taskExecutor;
	}

	@Bean
	@StepScope
	public ItemWriter<Customer> customItemWriter() {
		return new JdbcBatchItemWriterBuilder<Customer>()
			.dataSource(dataSource)
			.sql("insert into customer2 values (:id, :firstname, :lastname, :birthdate)")
			.beanMapped()
			.build();
	}

	@Bean
	@StepScope
	public ItemReader<? extends Customer> pagingItemReader(
		@Value("#{stepExecutionContext['minValue']}") Long minValue,
		@Value("#{stepExecutionContext['maxValue']}") Long maxValue
	) {
		System.out.println("reading : " + minValue + " to " + maxValue);

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, firstname, lastname, birthdate");
		queryProvider.setFromClause("from customer");
		queryProvider.setWhereClause("where id >= " + minValue + " and id <= " + maxValue);

		Map<String, Order> sortKeys = Map.of("id", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);

		return new JdbcPagingItemReaderBuilder<Customer>()
			.name("jdbcPagingItemReader")
			.dataSource(dataSource)
			.fetchSize(1000)
			.rowMapper(new CustomerRowMapper())
			.queryProvider(queryProvider)
			.build();
	}
}
