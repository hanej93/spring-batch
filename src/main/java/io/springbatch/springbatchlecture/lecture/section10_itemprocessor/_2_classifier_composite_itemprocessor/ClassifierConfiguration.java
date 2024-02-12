package io.springbatch.springbatchlecture.lecture.section10_itemprocessor._2_classifier_composite_itemprocessor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class ClassifierConfiguration {

	public static final int CHUNK_SIZE = 10;

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
			.<ProcessorInfo, ProcessorInfo>chunk(CHUNK_SIZE, transactionManager)
			.reader(new ItemReader<>() {
				int i = 0;
				@Override
				public ProcessorInfo read() throws
					Exception,
					UnexpectedInputException,
					ParseException,
					NonTransientResourceException {
					i++;
					ProcessorInfo processorInfo = ProcessorInfo.builder()
						.id(i)
						.build();
					return i > 3 ? null : processorInfo;
				}
			})
			.processor(customItemProcessor())
			.writer(items -> System.out.println("items = " + items))
			.build();
	}

	@Bean
	public ItemProcessor<? super ProcessorInfo, ? extends ProcessorInfo> customItemProcessor() {
		ClassifierCompositeItemProcessor<ProcessorInfo, ProcessorInfo> processor = new ClassifierCompositeItemProcessor<>();

		ProcessorClassifier<ProcessorInfo, ItemProcessor<?, ? extends ProcessorInfo>> classifier = new ProcessorClassifier<>();

		Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();
		processorMap.put(1, new CustomItemProcessor1());
		processorMap.put(2, new CustomItemProcessor2());
		processorMap.put(3, new CustomItemProcessor3());

		classifier.setProcessorMap(processorMap);
		processor.setClassifier(classifier);

		return processor;
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository).tasklet((contribution, chunkContext) -> {
			System.out.println("step2 has executed");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}
}
