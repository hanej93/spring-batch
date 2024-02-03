package io.springbatch.springbatchlecture.lecture.section8_itemreader._8_jpacursor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JdbcCursorConfiguration {

	public static final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(customerItemReader())
			.writer(customerItemWriter())
			.build();
	}

	@Bean
	public ItemReader<? extends Customer> customerItemReader() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("firstName", "A%");

		return new JpaCursorItemReaderBuilder<Customer>()
			.name("jpaCursorItemReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select c from Customer c where firstname like :firstName")
			.parameterValues(parameters)
			.build();
	}

	@Bean
	public ItemWriter<? super Customer> customerItemWriter() {
		return items -> {
			for (Customer item : items) {
				System.out.println("item = " + item);
			}
		};
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
