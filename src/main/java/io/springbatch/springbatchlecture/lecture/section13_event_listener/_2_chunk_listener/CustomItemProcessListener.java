package io.springbatch.springbatchlecture.lecture.section13_event_listener._2_chunk_listener;

import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessListener implements ItemProcessListener<Integer, String> {

	@Override
	public void beforeProcess(Integer item) {
		System.out.println("CustomItemProcessListener.beforeProcess");
	}

	@Override
	public void afterProcess(Integer item, String result) {
		System.out.println("CustomItemProcessListener.afterProcess");
	}

	@Override
	public void onProcessError(Integer item, Exception e) {
		System.out.println("CustomItemProcessListener.onProcessError");
	}
}
