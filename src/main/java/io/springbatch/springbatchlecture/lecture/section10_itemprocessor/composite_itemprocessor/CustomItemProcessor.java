package io.springbatch.springbatchlecture.lecture.section10_itemprocessor.composite_itemprocessor;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<String, String> {

	int cnt = 0;

	@Override
	public String process(String item) throws Exception {
		cnt++;
		return (item + cnt).toUpperCase();
	}
}
