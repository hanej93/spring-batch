package io.springbatch.springbatchlecture.lecture.section11_repeat_error._1_repeat;

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
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.springbatchlecture.lecture.section10_itemprocessor._2_classifier_composite_itemprocessor.CustomItemProcessor1;
import io.springbatch.springbatchlecture.lecture.section10_itemprocessor._2_classifier_composite_itemprocessor.CustomItemProcessor2;
import io.springbatch.springbatchlecture.lecture.section10_itemprocessor._2_classifier_composite_itemprocessor.CustomItemProcessor3;
import io.springbatch.springbatchlecture.lecture.section10_itemprocessor._2_classifier_composite_itemprocessor.ProcessorClassifier;
import io.springbatch.springbatchlecture.lecture.section10_itemprocessor._2_classifier_composite_itemprocessor.ProcessorInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RepeatConfiguration {

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
			.<String, String>chunk(CHUNK_SIZE, transactionManager)
			.reader(new ItemReader<>() {
				int i = 0;

				@Override
				public String read() throws
					Exception,
					UnexpectedInputException,
					ParseException,
					NonTransientResourceException {
					i++;
					return i > 3 ? null : "item" + i;
				}
			})
			.processor(new ItemProcessor<>() {
				RepeatTemplate repeatTemplate = new RepeatTemplate();

				@Override
				public String process(String item) throws Exception {
					// repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
					// repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));

					CompositeCompletionPolicy completionPolicy = new CompositeCompletionPolicy();
					CompletionPolicy[] completionPolicies = new CompletionPolicy[] {
						// new SimpleCompletionPolicy(3),
						new TimeoutTerminationPolicy(3000)
					};
					completionPolicy.setPolicies(completionPolicies);
					repeatTemplate.setCompletionPolicy(completionPolicy);

					repeatTemplate.setExceptionHandler(simpleLimitExceptionHandler());

					repeatTemplate.iterate(context -> {
						System.out.println("RepeatConfiguration.doInIteration item: " + item);
						throw new RuntimeException("Exception is occurred");
						// return RepeatStatus.CONTINUABLE;
					});

					return item;
				}
			})
			.writer(items -> System.out.println("items = " + items))
			.build();
	}

	@Bean
	public ExceptionHandler simpleLimitExceptionHandler() {
		return new SimpleLimitExceptionHandler(3);
	}

	@Bean
	public Step step2() {
		return new StepBuilder("step2", jobRepository).tasklet((contribution, chunkContext) -> {
			System.out.println("step2 has executed");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}
}
