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
public class FileSchJob extends QuartzJobBean {

	private final Job fileJob;
	private final JobLauncher jobLauncher;

	@SneakyThrows
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String requestDate = (String)context.getJobDetail().getJobDataMap().get("requestDate");

		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("id", new Date().getTime())
			.addString("requestDate", requestDate)
			.toJobParameters();

		jobLauncher.run(fileJob, jobParameters);
	}
}