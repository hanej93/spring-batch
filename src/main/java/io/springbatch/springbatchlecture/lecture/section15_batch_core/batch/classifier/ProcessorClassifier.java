package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.classifier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;

public class ProcessorClassifier<C, T> implements Classifier<C, T> {

	private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();

	@Override
	public T classify(C classifiable) {
		return (T) processorMap.get(((ProductVO)classifiable).getType());
	}

	public void setProcessorMap(
		Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap) {
		this.processorMap = processorMap;
	}
}
