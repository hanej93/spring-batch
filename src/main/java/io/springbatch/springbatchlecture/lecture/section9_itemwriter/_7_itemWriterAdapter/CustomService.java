package io.springbatch.springbatchlecture.lecture.section9_itemwriter._7_itemWriterAdapter;

public class CustomService<T> {

	public void customWrite(T item) {
		System.out.println("item = " + item);
	}
}
