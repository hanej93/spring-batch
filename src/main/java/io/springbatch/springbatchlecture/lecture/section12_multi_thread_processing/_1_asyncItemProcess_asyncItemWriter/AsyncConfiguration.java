package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._1_asyncItemProcess_asyncItemWriter;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class AsyncConfiguration {

	public static final int CHUNK_SIZE = 100;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;

	@Bean
	public Job batchJob() throws InterruptedException {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			// .start(step1())
			.start(asyncStep1())
			.next(step2())
			.listener(new StopWatchJobListener())
			.build();
	}

	@Bean
	public Step step1() throws InterruptedException {
		return new StepBuilder("step1", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(pagingItemReader())
			.processor(customItemProcessor())
			.writer(customItemWriter())
			.build();
	}

	@Bean
	public Step asyncStep1() throws InterruptedException {
		return new StepBuilder("asyncStep1", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(pagingItemReader())
			.processor(asyncItemProcessor())
			.writer(asyncItemWriter())
			.build();
	}

	@Bean
	public AsyncItemProcessor asyncItemProcessor() throws InterruptedException {
		AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(customItemProcessor());
		asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return asyncItemProcessor;
	}

	@Bean
	public AsyncItemWriter<Customer> asyncItemWriter() {
		AsyncItemWriter<Customer> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(customItemWriter());
		return asyncItemWriter;
	}

	@Bean
	public ItemProcessor<Customer, Customer> customItemProcessor() throws InterruptedException {
		return new ItemProcessor<Customer, Customer>() {
			@Override
			public Customer process(Customer item) throws Exception {
				Thread.sleep(1000);
				return new Customer(item.getId(),
					item.getFirstname().toUpperCase(),
					item.getLastname().toUpperCase(),
					item.getBirthdate());
			}
		};
	}

	@Bean
	public ItemWriter<Customer> customItemWriter() {
		return new JdbcBatchItemWriterBuilder<Customer>()
			.dataSource(dataSource)
			.sql("insert into customer2 values (:id, :firstname, :lastname, :birthdate)")
			.beanMapped()
			.build();
	}

	@Bean
	public ItemReader<? extends Customer> pagingItemReader() {
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, firstname, lastname, birthdate");
		queryProvider.setFromClause("from customer");
		queryProvider.setWhereClause("where firstname like :firstname");

		Map<String, Order> sortKeys = Map.of("id", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);

		Map<String, Object> parameters = Map.of("firstname", "A%");

		return new JdbcPagingItemReaderBuilder<Customer>()
			.name("jdbcPagingItemReader")
			.dataSource(dataSource)
			.fetchSize(CHUNK_SIZE)
			.rowMapper(new CustomerRowMapper())
			// .rowMapper(new BeanPropertyRowMapper<>())
			.queryProvider(queryProvider)
			.parameterValues(parameters)
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step2 has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
}
