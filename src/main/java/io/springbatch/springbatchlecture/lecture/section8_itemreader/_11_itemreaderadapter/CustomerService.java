package io.springbatch.springbatchlecture.lecture.section8_itemreader._11_itemreaderadapter;

public class CustomerService<T> {

	private int cnt = 0;

	public T customReader() {
		return (T) ("item" + cnt++);
	}
}
