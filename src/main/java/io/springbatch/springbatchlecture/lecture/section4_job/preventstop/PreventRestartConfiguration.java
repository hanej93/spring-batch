package io.springbatch.springbatchlecture.lecture.section4_job.preventstop;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class PreventRestartConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob1() {
		return new JobBuilder("batchJob1", jobRepository)
			.start(step1())
			.next(step2())
			.next(step3())
			.preventRestart()
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step1 was executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step2 was executed");
				// throw new RuntimeException("step2 was failed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step3() {
		return new StepBuilder("step3", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step3 was executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
}
