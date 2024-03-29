package io.springbatch.springbatchlecture.lecture.section12_multi_thread_processing._2_multi_thread_step;

import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessListener implements ItemProcessListener<Customer, Customer> {

	@Override
	public void beforeProcess(Customer item) {
	}

	@Override
	public void afterProcess(Customer item, Customer result) {
		System.out.println("Thread : " + Thread.currentThread().getName() + " process item : " + item.getId());
	}

	@Override
	public void onProcessError(Customer item, Exception e) {
	}
}
