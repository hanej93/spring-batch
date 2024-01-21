package io.springbatch.springbatchlecture.lecture.section7_chunk._4_itemreader_itemprocessor_itemwriter;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer customer) throws Exception {
		customer.setName(customer.getName().toUpperCase());
		return customer;
	}
}
