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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class HelloJobConfiguration {

	@Bean
	public Job helloJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("helloJob", jobRepository)
			.start(helloStep1(jobRepository, transactionManager))
			.next(helloStep2(jobRepository, transactionManager))
			.build();
	}

	@Bean
	public Step helloStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("helloStep1", jobRepository)
			.tasklet((contribution, chunkContext) -> {

				System.out.println("=========================");
				System.out.println(" >> Hello Spring Batch!");
				System.out.println("=========================");

				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step helloStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("helloStep2", jobRepository)
			.tasklet((contribution, chunkContext) -> {

				System.out.println("=========================");
				System.out.println(" >> step2 was executed");
				System.out.println("=========================");

				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

}
