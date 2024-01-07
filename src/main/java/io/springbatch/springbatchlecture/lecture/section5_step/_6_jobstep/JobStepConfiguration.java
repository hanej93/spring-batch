package io.springbatch.springbatchlecture.lecture.section5_step._6_jobstep;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class JobStepConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job parentJob() {
		return new JobBuilder("parentJob", jobRepository)
			.start(jobStep(null))
			.next(step2())
			.build();
	}

	@Bean
	public Step jobStep(JobLauncher jobLauncher) {
		return new StepBuilder("jobStep", jobRepository)
			.job(childJob())
			.launcher(jobLauncher)
			.parametersExtractor(jobParameterExtractor())
			.listener(new StepExecutionListener() {
				@Override
				public void beforeStep(StepExecution stepExecution) {
					stepExecution.getExecutionContext().putString("name", "user1");
				}

				@Override
				public ExitStatus afterStep(StepExecution stepExecution) {
					return null;
				}
			})
			.build();
	}

	private DefaultJobParametersExtractor jobParameterExtractor() {
		DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
		extractor.setKeys(new String[]{"name"});
		return extractor;
	}

	@Bean
	public Job childJob() {
		return new JobBuilder("childJob", jobRepository)
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				// throw new RuntimeException("step1 was failed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				throw new RuntimeException("step2 was failed");
				// return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
}
