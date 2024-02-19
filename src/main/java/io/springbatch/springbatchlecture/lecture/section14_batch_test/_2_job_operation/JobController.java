package io.springbatch.springbatchlecture.lecture.section14_batch_test._2_job_operation;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JobController {

	private final JobRegistry jobRegistry;
	private final JobExplorer jobExplorer;
	private final JobOperator jobOperator;

	@PostMapping("/batch/start")
	public String start(@RequestBody JobInfo jobInfo) throws
		NoSuchJobException,
		JobInstanceAlreadyExistsException,
		JobParametersInvalidException {

		for (Iterator<String> iterator = jobRegistry.getJobNames().stream().iterator(); iterator.hasNext(); ) {
			SimpleJob job = (SimpleJob) jobRegistry.getJob(iterator.next());
			System.out.println("job.getName() = " + job.getName());

			Properties properties = new Properties();
			properties.put("id", jobInfo.getId());
			jobOperator.start(job.getName(), properties);
		}
		
		return "batch is started";
	}

	@PostMapping("/batch/stop")
	public String stop() throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {
		for (Iterator<String> iterator = jobRegistry.getJobNames().stream().iterator(); iterator.hasNext(); ) {
			SimpleJob job = (SimpleJob) jobRegistry.getJob(iterator.next());
			System.out.println("job.getName() = " + job.getName());

			Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(job.getName());
			JobExecution jobExecution = runningJobExecutions.iterator().next();

			jobOperator.stop(jobExecution.getId());
		}

		return "batch is stopped";
	}

	@PostMapping("/batch/restart")
	public String restart() throws
		NoSuchJobException,
		NoSuchJobExecutionException,
		JobInstanceAlreadyCompleteException,
		JobParametersInvalidException,
		JobRestartException {
		for (Iterator<String> iterator = jobRegistry.getJobNames().stream().iterator(); iterator.hasNext(); ) {
			SimpleJob job = (SimpleJob) jobRegistry.getJob(iterator.next());
			System.out.println("job.getName() = " + job.getName());

			JobInstance lastJobInstance = jobExplorer.getLastJobInstance(job.getName());

			jobOperator.restart(lastJobInstance.getId());
		}

		return "batch is restarted";
	}

}
