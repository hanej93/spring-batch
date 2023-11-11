package io.springbatch.springbatchlecture.lecture.section3.jobparameter;

import java.util.Date;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

// @Configuration
public class JobParameterConfiguration {

	@Bean
	public Job job(JobRepository jobRepository, Step step1, Step step2) {
		return new JobBuilder("job", jobRepository)
			.start(step1)
			.next(step2)
			.build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step1", jobRepository)
			.tasklet((contribution, chunkContext) -> {

				JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
				String name = jobParameters.getString("name");
				Long seq = jobParameters.getLong("seq");
				Date date = jobParameters.getDate("date");
				Double age = jobParameters.getDouble("age");

				System.out.println("name = " + name);
				System.out.println("seq = " + seq);
				System.out.println("date = " + date);
				System.out.println("age = " + age);

				Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();

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
