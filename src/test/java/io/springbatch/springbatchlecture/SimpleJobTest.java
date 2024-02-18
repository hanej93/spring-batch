package io.springbatch.springbatchlecture;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Date;

import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import io.springbatch.springbatchlecture.lecture.section14_batch_test._1_spring_batch_test.SimpleJobConfiguration;

@SpringBootTest(classes = {SimpleJobConfiguration.class, TestBatchConfig.class})
@SpringBatchTest
public class SimpleJobTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@AfterEach
	public void clear() {
		jdbcTemplate.execute("delete from customer2");
	}

	@Test
	public void simpleJob_test() throws Exception {
		// given
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("name", "user1")
			.addLong("date", new Date().getTime())
			.toJobParameters();

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		// then
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
	}

	@Test
	public void simpleStep_test() {
		// given
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("name", "user1")
			.addLong("date", new Date().getTime())
			.toJobParameters();

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1");

		// then
		StepExecution stepExecution = jobExecution.getStepExecutions().stream().toList().get(0);
		assertThat(stepExecution.getCommitCount()).isEqualTo(10 + 1);
		assertThat(stepExecution.getReadCount()).isEqualTo(1000);
		assertThat(stepExecution.getWriteCount()).isEqualTo(1000);
	}
}
