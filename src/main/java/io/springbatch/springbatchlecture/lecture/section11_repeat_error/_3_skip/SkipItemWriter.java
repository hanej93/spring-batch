package io.springbatch.springbatchlecture.lecture.section11_repeat_error._3_skip;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class SkipItemWriter implements ItemWriter<String> {

	private int cnt;

	@Override
	public void write(Chunk<? extends String> chunk) throws Exception {
		for (String item : chunk) {
			if ("-12".equals(item)) {
				System.out.println("SkipItemWriter.write[1] item=" + item);
				throw new SkippableException("Writer failed cnt=" + cnt);
			}
			System.out.println("SkipItemWriter.write[2] item=" + item);
		}
	}
}
