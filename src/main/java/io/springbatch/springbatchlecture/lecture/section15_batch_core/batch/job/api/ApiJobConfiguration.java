package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.job.api;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.listener.JobListener;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.tasklet.ApiEndTasklet;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.tasklet.ApiStartTasklet;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfiguration {

	public static final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	private final ApiStartTasklet apiStartTasklet;
	private final ApiEndTasklet apiEndTasklet;
	private final Step jobStep;

	@Bean
	public Job apiJob() {
		return new JobBuilder("apiJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.listener(new JobListener())
			.start(apiStep1())
			.next(jobStep)
			.next(apiStep2())
			.build();
	}

	@Bean
	public Step apiStep1() {
		return new StepBuilder("apiStep1", jobRepository)
			.tasklet(apiStartTasklet, transactionManager)
			.build();
	}

	@Bean
	public Step apiStep2() {
		return new StepBuilder("apiStep2", jobRepository)
			.tasklet(apiEndTasklet, transactionManager)
			.build();
	}
}
