package io.springbatch.springbatchlecture.lecture.section13_event_listener._2_chunk_listener;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._2_multi_thread_step.CustomItemWriteListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ChunkListenerConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.<Integer, String>chunk(5, transactionManager)
			.listener(new CustomChunkListener())
			.listener(new CustomItemReadListener())
			.listener(new CustomItemProcessListener())
			.listener(new CustomItemWriteListener())
			.reader(listItemReader())
			.processor(item -> {
				throw new RuntimeException();
				// return "item" + item;
			})
			.writer(chunk -> System.out.println("chunk = " + chunk))
			.build();
	}

	@Bean
	public ItemReader<Integer> listItemReader() {
		List<Integer> list = IntStream.rangeClosed(1, 10)
			.boxed()
			.toList();
		return new ListItemReader<>(list);
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
