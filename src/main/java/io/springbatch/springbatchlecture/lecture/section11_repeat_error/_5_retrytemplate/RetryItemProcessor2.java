package io.springbatch.springbatchlecture.lecture.section11_repeat_error._5_retrytemplate;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.classify.Classifier;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

import io.springbatch.springbatchlecture.lecture.section11_repeat_error._4_retry.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryItemProcessor2 implements ItemProcessor<String, Customer> {

	@Autowired
	private RetryTemplate retryTemplate;

	private int cnt;

	@Override
	public Customer process(String item) throws Exception {
		Classifier<Throwable, Boolean> rollbackClassifier = new BinaryExceptionClassifier(true);

		Customer customer = retryTemplate.execute(new RetryCallback<Customer, RuntimeException>() {
			@Override
			public Customer doWithRetry(RetryContext context) throws RuntimeException {
				if ("1".equals(item) || "2".equals(item)) {
					cnt++;
					log.info("RetryItemProcessor2.doWithRetry Failed cnt: {}, item: {}", cnt, item);
					throw new RetryableException("failed cnt : " + cnt);
				}
				log.info("RetryItemProcessor2.doWithRetry item: {}", item);
				return new Customer(item);
			}
		}, new RecoveryCallback<Customer>() {
			@Override
			public Customer recover(RetryContext context) throws Exception {
				log.info("RetryItemProcessor2.recover item: {}", item);
				return new Customer(item);
			}
		}, new DefaultRetryState(item, rollbackClassifier));
		return customer;
	}
}
