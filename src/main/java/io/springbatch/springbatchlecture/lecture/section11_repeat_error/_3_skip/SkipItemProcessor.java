package io.springbatch.springbatchlecture.lecture.section11_repeat_error._3_skip;

import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String> {

	private int cnt;

	@Override
	public String process(String item) throws Exception {
		if ("6".equals(item) || "7".equals(item)) {
			System.out.println("SkipItemProcessor.process[1] item=" + item);
			throw new SkippableException("Process failed cnt=" + cnt);
		}
		System.out.println("SkipItemProcessor.process[2] item=" + item);
		return String.valueOf(Integer.valueOf(item) * -1);
	}
}
