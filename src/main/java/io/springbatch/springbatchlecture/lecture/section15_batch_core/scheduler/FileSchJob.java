package io.springbatch.springbatchlecture.lecture.section15_batch_core.scheduler;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
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
	private final JobExplorer jobExplorer;

	@SneakyThrows
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String requestDate = (String)context.getJobDetail().getJobDataMap().get("requestDate");

		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("id", new Date().getTime())
			.addString("requestDate", requestDate)
			.toJobParameters();

		int jobInstanceCount = (int) jobExplorer.getJobInstanceCount(fileJob.getName());
		List<JobInstance> jobInstances = jobExplorer.getJobInstances(fileJob.getName(), 0, jobInstanceCount);

		if (!jobInstances.isEmpty()) {
			for (JobInstance jobInstance : jobInstances) {
				List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
				boolean hasRequestDateInJobExecution = jobExecutions.stream()
					.anyMatch(
						jobExecution -> requestDate.equals(jobExecution.getJobParameters().getString("requestDate")));
				if (hasRequestDateInJobExecution) {
					throw new JobExecutionException(requestDate + " already exists");
				}
			}
		}

		jobLauncher.run(fileJob, jobParameters);
	}
}
