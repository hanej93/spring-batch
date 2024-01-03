package io.springbatch.springbatchlecture;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section5._3_tasklet.CustomTasklet;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class Limit_AllowConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

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
			.tasklet((contribution, chunkContext) -> {
				System.out.println("contribution = " + contribution);
				System.out.println("chunkContext = " + chunkContext);
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("contribution = " + contribution);
				System.out.println("chunkContext = " + chunkContext);
				throw new RuntimeException("step2 was failed");
				// return RepeatStatus.FINISHED;
			}, transactionManager)
			.startLimit(3)
			.build();
	}
}
