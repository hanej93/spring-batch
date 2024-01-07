package io.springbatch.springbatchlecture.lecture.section4_job.jobbuilderfactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class JobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	// @Bean
	// public Job batchJob1() {
	// 	return new JobBuilder("batchJob1", jobRepository)
	// 		.start(step1())
	// 		.next(step2())
	// 		.build();
	// }

	@Bean
	public Job batchJob2() {
		return new JobBuilder("batchJob2", jobRepository)
			.start(flow1())
			.next(step5())
			.end()
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

	@Bean
	public Step step4() {
		return new StepBuilder("step4", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step4 was executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step5() {
		return new StepBuilder("step5", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step5 was executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Flow flow1() {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");
		flowBuilder.start(step3())
			.next(step4())
			.end();
		return flowBuilder.build();
	}
}
