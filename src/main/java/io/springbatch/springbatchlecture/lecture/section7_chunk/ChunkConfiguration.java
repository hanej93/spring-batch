package io.springbatch.springbatchlecture.lecture.section7_chunk;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ChunkConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.<String, String>chunk(5, transactionManager)
			.reader(new ListItemReader<>(Arrays.asList("item2", "item2", "item3", "item4", "item5")))
			.processor(item -> {
				Thread.sleep(300);
				System.out.println("item = " + item);
				return "my" + item;
			})
			.writer(chunk -> {
				Thread.sleep(300);
				System.out.println("chunk = " + chunk);
			})
			.build();
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				System.out.println("step2 has executed");
				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}
}
