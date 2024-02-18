package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._5_not_synchronized;

import javax.sql.DataSource;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class SynchronizedConfiguration {

	public static final int CHUNK_SIZE = 100;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;

	@Bean
	public Job batchJob() throws InterruptedException {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.listener(new StopWatchJobListener())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(customItemReader())
			.listener(new ItemReadListener<>() {
				@Override
				public void afterRead(Customer item) {
					System.out.println("Thread : " + Thread.currentThread().getName() + ", item.getId() : " + item.getId());
				}
			})
			.writer(customItemWriter())
			.taskExecutor(taskExecutor())
			.build();
	}

	@Bean
	public SynchronizedItemStreamReader<Customer> customItemReader() {
		JdbcCursorItemReader<Customer> notSafeItemReader = new JdbcCursorItemReaderBuilder<Customer>()
			.name("NotSafeItemReader")
			.fetchSize(CHUNK_SIZE)
			.sql("select id, firstName, lastName, birthdate from customer")
			.beanRowMapper(Customer.class)
			.dataSource(dataSource)
			.build();
		return new SynchronizedItemStreamReaderBuilder<Customer>()
			.delegate(notSafeItemReader)
			.build();
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
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(4);
		taskExecutor.setMaxPoolSize(8);
		taskExecutor.setThreadNamePrefix("not-safety-thread");

		return taskExecutor;
	}
}
