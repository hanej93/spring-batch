package io.springbatch.springbatchlecture.lecture.section11_repeat_error._4_retry;

import org.springframework.batch.item.ItemProcessor;

public class RetryItemProcessor implements ItemProcessor<String, String> {

	private int cnt = 0;

	@Override
	public String process(String item) throws Exception {
		if ("2".equals(item) || "3".equals(item)) {
			cnt++;
			System.out.println("RetryItemProcessor.process[error] item=" + item);
			System.out.println("RetryItemProcessor.process[error] cnt=" + cnt);
			throw new RetryableException();
		}
		System.out.println("RetryItemProcessor.process[normal] item=" + item);
		return item;
	}
}
