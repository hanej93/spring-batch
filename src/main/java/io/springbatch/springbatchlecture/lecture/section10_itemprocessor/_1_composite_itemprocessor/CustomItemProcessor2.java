package io.springbatch.springbatchlecture.lecture.section10_itemprocessor._1_composite_itemprocessor;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor2 implements ItemProcessor<String, String> {

	int cnt = 0;

	@Override
	public String process(String item) throws Exception {
		cnt++;
		return (item + cnt).toUpperCase();
	}
}
