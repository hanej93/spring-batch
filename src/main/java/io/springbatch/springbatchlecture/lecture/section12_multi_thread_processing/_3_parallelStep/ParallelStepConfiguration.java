package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._3_parallelStep;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class ParallelStepConfiguration {

	public static final int CHUNK_SIZE = 100;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() throws InterruptedException {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(flow1())
			// .next(flow2())
			.split(taskExecutor()).add(flow2())
			.end()
			.listener(new StopWatchJobListener())
			.build();
	}

	@Bean
	public Flow flow1() {
		TaskletStep step1 = new StepBuilder("step1", jobRepository)
			.tasklet(tasklet(), transactionManager)
			.build();

		return new FlowBuilder<Flow>("flow1")
			.start(step1)
			.build();
	}

	@Bean
	public Flow flow2() {
		TaskletStep step2 = new StepBuilder("step2", jobRepository)
			.tasklet(tasklet(), transactionManager)
			.build();
		TaskletStep step3 = new StepBuilder("step3", jobRepository)
			.tasklet(tasklet(), transactionManager)
			.build();

		return new FlowBuilder<Flow>("flow2")
			.start(step2)
			.next(step3)
			.build();
	}

	@Bean
	public Tasklet tasklet() {
		return new CustomTasklet();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(2);
		taskExecutor.setMaxPoolSize(4);
		taskExecutor.setThreadNamePrefix("async-thread");

		return taskExecutor;
	}
}
