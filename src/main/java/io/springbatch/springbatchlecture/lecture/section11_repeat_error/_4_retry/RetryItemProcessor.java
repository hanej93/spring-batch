package io.springbatch.springbatchlecture.lecture.section11_repeat_error._4_retry;

import org.springframework.batch.item.ItemProcessor;

public class RetryItemProcessor implements ItemProcessor<String, String> {

	private int cnt = 0;

	@Override
	public String process(String item) throws Exception {
		cnt++;
		System.out.println("RetryItemProcessor.process cnt=" + cnt);
		throw new RetryableException();
		// return null;
	}
}
