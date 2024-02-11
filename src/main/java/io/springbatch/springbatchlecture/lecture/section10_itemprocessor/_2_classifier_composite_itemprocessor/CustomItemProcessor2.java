package io.springbatch.springbatchlecture.lecture.section10_itemprocessor._2_classifier_composite_itemprocessor;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor2 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {

	@Override
	public ProcessorInfo process(ProcessorInfo item) throws Exception {
		System.out.println("CustomItemProcessor2.process");
		return item;
	}
}
