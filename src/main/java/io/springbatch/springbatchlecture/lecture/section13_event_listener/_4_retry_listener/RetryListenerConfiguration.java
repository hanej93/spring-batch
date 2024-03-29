package io.springbatch.springbatchlecture.lecture.section13_event_listener._4_retry_listener;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
// @Configuration
public class RetryListenerConfiguration {

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
			.<Integer, String>chunk(5, transactionManager)
			.reader(listItemReader())
			.processor(new CustomItemProcessor())
			.writer(new CustomItemWriter())
			.faultTolerant()
			.retry(CustomRetryException.class)
			.retryLimit(2)
			.listener(new CustomRetryListener())
			.build();
	}

	@Bean
	public ItemReader<Integer> listItemReader() {
		List<Integer> list = IntStream.rangeClosed(1, 10)
			.boxed()
			.toList();
		System.out.println("reader list = " + list);
		return new LinkedItemReader<>(list);
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
