package io.springbatch.springbatchlecture.lecture.section13_event_listener._4_retry_listener;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<String> {

	int count = 0;

	@Override
	public void write(Chunk<? extends String> chunk) throws Exception {
		chunk.forEach(item -> {
			if (count < 2) {
				if (count % 2 == 0) {
					count++;
				} else {
					count++;
					throw new CustomRetryException("failed");
				}
			}
			System.out.println("write : " + item);
		});
	}
}
