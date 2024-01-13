package io.springbatch.springbatchlecture.lecture.section6_flow._6_jobexecutiondecider;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section6_flow._5_customexitstatus.PassCheckingListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JobExecutionDeciderConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step())
			.next(decider())
			.from(decider()).on("ODD").to(oddStep())
			.from(decider()).on("EVEN").to(evenStep())
			.end()
			.build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new CustomDecider();
	}

	@Bean
	public Step step() {
		return new StepBuilder("startStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("startStep has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step evenStep() {
		return new StepBuilder("evenStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("evenStep has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step oddStep() {
		return new StepBuilder("oddStep", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("oddStep has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
}
