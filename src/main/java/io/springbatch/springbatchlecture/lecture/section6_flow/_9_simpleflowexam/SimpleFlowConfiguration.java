package io.springbatch.springbatchlecture.lecture.section6_flow._9_simpleflowexam;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SimpleFlowConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.start(flow1())
				.on("COMPLETED")
				.to(flow2())
			.from(flow1())
				.on("FAILED")
				.to(flow3())
			.end()
			.build();
	}

	@Bean
	Flow flow1() {
		return new FlowBuilder<Flow>("flow1")
			.start(step1())
			.next(step2())
			.end();
	}

	@Bean
	Flow flow2() {
		return new FlowBuilder<Flow>("flow2")
			.start(flow3())
			.next(step5())
			.next(step6())
			.end();
	}

	@Bean
	Flow flow3() {
		return new FlowBuilder<Flow>("flow3")
			.start(step3())
			.next(step4())
			.end();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step1 has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step2 has executed");
				throw new RuntimeException("step2 was failed");
				// return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step3() {
		return new StepBuilder("step3", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step3 has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step4() {
		return new StepBuilder("step4", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step4 has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step5() {
		return new StepBuilder("step5", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step5 has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step6() {
		return new StepBuilder("step6", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step6 has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
}
