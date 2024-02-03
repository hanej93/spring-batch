package io.springbatch.springbatchlecture.lecture.section8_itemreader._9_jdbcpaging;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JdbcPagingConfiguration {

	public static final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;

	@Bean
	public Job batchJob() throws Exception {
		return new JobBuilder("batchJob", jobRepository)
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean
	public Step step1() throws Exception {
		return new StepBuilder("step1", jobRepository)
			.<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
			.reader(customerItemReader())
			.writer(customerItemWriter())
			.build();
	}

	@Bean
	public ItemReader<? extends Customer> customerItemReader() throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("firstname", "A%");

		return new JdbcPagingItemReaderBuilder<Customer>()
			.name("jdbcPagingReader")
			.pageSize(CHUNK_SIZE)
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(Customer.class))
			.queryProvider(createQueryProvider())
			.parameterValues(parameters)
			.build();
	}

	@Bean
	public PagingQueryProvider createQueryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("id, firstname, lastname, birthdate");
		queryProvider.setFromClause("from customer");
		queryProvider.setWhereClause("where firstname like :firstname");

		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("id", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);

		return queryProvider.getObject();
	}

	@Bean
	public ItemWriter<? super Customer> customerItemWriter() {
		return items -> {
			for (Customer item : items) {
				System.out.println("item = " + item);
			}
		};
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
