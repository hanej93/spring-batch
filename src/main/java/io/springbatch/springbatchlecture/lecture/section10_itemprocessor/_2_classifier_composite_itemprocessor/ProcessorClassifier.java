package io.springbatch.springbatchlecture.lecture.section10_itemprocessor._2_classifier_composite_itemprocessor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

public class ProcessorClassifier<C, T> implements Classifier<C, T> {

	private Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();

	@Override
	public T classify(C classifiable) {
		return (T) processorMap.get(((ProcessorInfo)classifiable).getId());
	}

	public void setProcessorMap(
		Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap
	) {
		this.processorMap = processorMap;
	}
}
