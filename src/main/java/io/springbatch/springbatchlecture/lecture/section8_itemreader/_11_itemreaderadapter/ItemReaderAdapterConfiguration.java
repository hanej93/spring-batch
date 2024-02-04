package io.springbatch.springbatchlecture.lecture.section8_itemreader._11_itemreaderadapter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class ItemReaderAdapterConfiguration {

	public static final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job batchJob() {
		return new JobBuilder("batchJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
			.<String, String>chunk(CHUNK_SIZE, transactionManager)
			.reader(customerItemReader())
			.writer(customerItemWriter())
			.build();
	}

	@Bean
	public ItemReader<String> customerItemReader() {
		ItemReaderAdapter<String> reader = new ItemReaderAdapter<>();
		reader.setTargetObject(customerService());
		reader.setTargetMethod("customReader");
		return reader;
	}

	@Bean
	public Object customerService() {
		return new CustomerService();
	}

	@Bean
	public ItemWriter<String> customerItemWriter() {
		return items -> {
			for (String item : items) {
				System.out.println("item = " + item);
			}
		};
	}
}
