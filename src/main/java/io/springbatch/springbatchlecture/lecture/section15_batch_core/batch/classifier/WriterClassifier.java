package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.classifier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;

public class WriterClassifier<C, T> implements Classifier<C, T> {

	private Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();

	@Override
	public T classify(C classifiable) {
		return (T) writerMap.get(((ApiRequestVO)classifiable).getProductVO().getType());
	}

	public void setWriterMap(Map<String, ItemWriter<ApiRequestVO>> writerMap) {
		this.writerMap = writerMap;
	}
}
