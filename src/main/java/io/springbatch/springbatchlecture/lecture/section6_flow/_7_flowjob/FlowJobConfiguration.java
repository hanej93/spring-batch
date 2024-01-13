package io.springbatch.springbatchlecture.lecture.section6_flow._7_flowjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section6_flow._6_jobexecutiondecider.CustomDecider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.start(flow())
			.next(step3())
			.end()
			.build();
	}

	@Bean
	public Flow flow() {
		return new FlowBuilder<Flow>("flow")
			.start(step1())
			.next(step2())
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
				return RepeatStatus.FINISHED;
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
}
