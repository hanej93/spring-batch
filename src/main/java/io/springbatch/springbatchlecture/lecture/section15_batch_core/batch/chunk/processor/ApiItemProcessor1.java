package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.processor;

import org.springframework.batch.item.ItemProcessor;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;

public class ApiItemProcessor1 implements ItemProcessor<ProductVO, ApiRequestVO> {

	@Override
	public ApiRequestVO process(ProductVO item) throws Exception {
		return ApiRequestVO.builder()
			.id(item.getId())
			.productVO(item)
			.build();
	}
}
