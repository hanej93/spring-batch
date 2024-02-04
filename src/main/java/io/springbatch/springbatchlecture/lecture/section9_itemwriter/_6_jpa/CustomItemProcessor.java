package io.springbatch.springbatchlecture.lecture.section9_itemwriter._6_jpa;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Customer, Customer2> {

	ModelMapper modelMapper = new ModelMapper();

	@Override
	public Customer2 process(Customer item) throws Exception {
		return modelMapper.map(item, Customer2.class);
	}
}
