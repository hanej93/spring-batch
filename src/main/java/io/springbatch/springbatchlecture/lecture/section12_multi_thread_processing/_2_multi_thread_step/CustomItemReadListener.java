package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._2_multi_thread_step;

import org.springframework.batch.core.ItemReadListener;

public class CustomItemReadListener implements ItemReadListener<Customer> {

	@Override
	public void beforeRead() {
	}

	@Override
	public void afterRead(Customer item) {
		System.out.println("Thread : " + Thread.currentThread().getName() + " read item : " + item.getId());
	}

	@Override
	public void onReadError(Exception ex) {
	}
}
