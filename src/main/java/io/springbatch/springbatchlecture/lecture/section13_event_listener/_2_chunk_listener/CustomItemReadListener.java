package io.springbatch.springbatchlecture.lecture.section13_event_listener._2_chunk_listener;

import org.springframework.batch.core.ItemReadListener;

public class CustomItemReadListener implements ItemReadListener<Integer> {

	@Override
	public void beforeRead() {
		System.out.println("CustomItemReadListener.beforeRead");
	}

	@Override
	public void afterRead(Integer item) {
		System.out.println("CustomItemReadListener.afterRead");
	}

	@Override
	public void onReadError(Exception ex) {
		System.out.println("CustomItemReadListener.onReadError");
	}
}
