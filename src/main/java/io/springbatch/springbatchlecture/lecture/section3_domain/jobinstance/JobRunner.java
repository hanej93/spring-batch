package io.springbatch.springbatchlecture.lecture.section3_domain.jobinstance;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import lombok.RequiredArgsConstructor;

//@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {

	private final JobLauncher jobLauncher;
	private final Job job;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("name", "user2")
			.toJobParameters();

		jobLauncher.run(job, jobParameters);
	}
}
