package io.springbatch.springbatchlecture.lecture.section15_batch_core.scheduler;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class ApiSchJob extends QuartzJobBean {

	private final Job apiJob;
	private final JobLauncher jobLauncher;

	@SneakyThrows
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("id", new Date().getTime())
			.toJobParameters();

		jobLauncher.run(apiJob, jobParameters);
	}
}
