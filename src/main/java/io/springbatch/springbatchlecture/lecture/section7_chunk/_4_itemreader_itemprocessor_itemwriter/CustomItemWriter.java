package io.springbatch.springbatchlecture.lecture.section7_chunk._4_itemreader_itemprocessor_itemwriter;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<Customer> {

	@Override
	public void write(Chunk<? extends Customer> chunk) throws Exception {
		chunk.forEach(item -> System.out.println("item = " + item));
	}
}
