package io.springbatch.springbatchlecture.lecture.section9_itemwriter._6_jpa;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class JpaBatchConfiguration {

	public static final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.<Customer, Customer2>chunk(CHUNK_SIZE, transactionManager)
			.reader(customItemReader())
			.processor(customItemProcessor())
			.writer(customItemWriter())
			.build();
	}

	@Bean
	public ItemWriter<? super Customer2> customItemWriter() {
		return new JpaItemWriterBuilder<Customer2>()
			.usePersist(true)
			.entityManagerFactory(entityManagerFactory)
			.build();
	}

	@Bean
	public ItemProcessor<? super Customer, ? extends Customer2> customItemProcessor() {
		return new CustomItemProcessor();
	}

	@Bean
	public ItemReader<? extends Customer> customItemReader() {
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
