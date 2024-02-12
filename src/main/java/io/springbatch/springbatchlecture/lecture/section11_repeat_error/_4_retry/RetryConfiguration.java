package io.springbatch.springbatchlecture.lecture.section11_repeat_error._4_retry;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RetryConfiguration {

	public static final int CHUNK_SIZE = 5;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

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
			.<String, String>chunk(CHUNK_SIZE, transactionManager)
			.reader(reader())
			.processor(processor())
			.writer(items -> items.forEach(item -> System.out.println("item = " + item)))
			.faultTolerant()
			.retry(RetryableException.class)
			.retryLimit(2)
			.build();
	}

	@Bean
	public ItemProcessor<? super String, String> processor() {
		return new RetryItemProcessor();
	}

	@Bean
	public ListItemReader<String> reader() {
		List<String> items = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			items.add(String.valueOf(i));
		}
		return new ListItemReader<>(items);
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository).tasklet((contribution, chunkContext) -> {
			System.out.println("step2 has executed");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}
}
