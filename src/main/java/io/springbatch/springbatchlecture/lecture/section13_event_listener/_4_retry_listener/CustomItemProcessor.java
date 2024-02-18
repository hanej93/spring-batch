package io.springbatch.springbatchlecture.lecture.section13_event_listener._4_retry_listener;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Integer, String> {

	int count = 0;

	@Override
	public String process(Integer item) throws Exception {
		if (count < 2) {
			if (count % 2 == 0) {
				count++;
			} else {
				count++;
				throw new CustomRetryException("failed");
			}
		}

		return String.valueOf(item);
	}
}
