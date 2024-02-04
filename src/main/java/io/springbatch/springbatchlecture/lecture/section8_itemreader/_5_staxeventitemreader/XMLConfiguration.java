package io.springbatch.springbatchlecture.lecture.section8_itemreader._5_staxeventitemreader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class XMLConfiguration {

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
			.<Customer, Customer>chunk(5, transactionManager)
			.reader(customerItemReader())
			.writer(chunk -> {
				chunk.forEach(item -> System.out.println("item = " + item));
				System.out.println("");
			})
			.build();
	}

	@Bean
	public ItemReader<? extends Customer> customerItemReader() {
		return new StaxEventItemReaderBuilder<Customer>()
			.name("staxxml")
			.resource(new ClassPathResource("files/customer.xml"))
			.addFragmentRootElements("customer")
			.unmarshaller(itemUnmarshaller())
			.build();
	}

	@Bean
	public Unmarshaller itemUnmarshaller() {
		Map<String, Class<?>> aliases = new HashMap<>();
		aliases.put("customer", Customer.class);
		aliases.put("id", Long.class);
		aliases.put("name", String.class);
		aliases.put("age", Integer.class);

		XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		xStreamMarshaller.setAliases(aliases);
		return xStreamMarshaller;
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
