package io.springbatch.springbatchlecture.lecture.section3_domain.jobrepository;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
//@Configuration
public class JobRepositoryConfiguration {

	private final JobExecutionListener jobRepositoryListener;

	@Bean
	public Job job(JobRepository jobRepository, Step step1, Step step2) {
		return new JobBuilder("batchJob", jobRepository)
			.start(step1)
			.next(step2)
			.listener(jobRepositoryListener)
			.build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step1", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step1 was executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step2", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step2 was executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
}
