package io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.chunk.processor;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.Product;
import io.springbatch.springbatchlecture.lecture.section15_batch_core.batch.domain.ProductVO;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {

	@Override
	public Product process(ProductVO item) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(item, Product.class);
	}
}
