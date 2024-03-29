package io.springbatch.springbatchlecture.lecture.section7_chunk._4_itemreader_itemprocessor_itemwriter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CustomItemReader implements ItemReader<Customer> {

	private List<Customer> list;

	public CustomItemReader(List<Customer> list) {
		this.list = new ArrayList<>(list);
	}

	@Override
	public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (!list.isEmpty()) {
			return list.remove(0);
		}

		return null;
	}
}
