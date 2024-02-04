package io.springbatch.springbatchlecture.lecture.section9_itemwriter._3_xml_staxEvent;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Configuration
public class XmlConfiguration {

	public static final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;

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
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(customerItemReader())
			.writer(customerItemWriter())
			.build();
	}

	@Bean
	public ItemWriter<? super Customer> customerItemWriter() {
		return new StaxEventItemWriterBuilder<Customer>()
			.name("staxEventWriter")
			.marshaller(itemMarshaller())
			.resource(new FileSystemResource("C:\\Users\\hanej\\Desktop\\Study\\정수원\\spring-batch\\springbatch\\src\\main\\resources\\files\\customer.xml"))
			.rootTagName("customer")
			.build();
	}

	@Bean
	public Marshaller itemMarshaller() {
		Map<String, Class<?>> aliases = new HashMap<>();
		aliases.put("customer", Customer.class);
		aliases.put("id", Long.class);
		aliases.put("firstname", String.class);
		aliases.put("lastname", String.class);
		aliases.put("birthdate", String.class);

		XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		xStreamMarshaller.setAliases(aliases);

		return xStreamMarshaller;
	}

	@Bean
	public ItemReader<? extends Customer> customerItemReader() {
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, firstname, lastname, birthdate");
		queryProvider.setFromClause("from customer");
		queryProvider.setWhereClause("where firstname like :firstname");

		Map<String, Order> sortKeys = Map.of("id", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);

		Map<String, Object> parameters = Map.of("firstname", "A%");

		return new JdbcPagingItemReaderBuilder<Customer>()
			.name("jdbcPagingItemReader")
			.dataSource(dataSource)
			.fetchSize(CHUNK_SIZE)
			.rowMapper(new CustomerRowMapper())
			// .rowMapper(new BeanPropertyRowMapper<>())
			.queryProvider(queryProvider)
			.parameterValues(parameters)
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
