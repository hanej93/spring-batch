package io.springbatch.springbatchlecture.lecture.section13_event_listener._3_skip_listener;

import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener<Integer, String> {

	@Override
	public void onSkipInRead(Throwable t) {
		System.out.println("CustomSkipListener.onSkipInRead : " + t.getMessage());
	}

	@Override
	public void onSkipInWrite(String item, Throwable t) {
		System.out.println("CustomSkipListener.onSkipInWrite : " + item);
		System.out.println("CustomSkipListener.onSkipInWrite : " + t.getMessage());
	}

	@Override
	public void onSkipInProcess(Integer item, Throwable t) {
		System.out.println("CustomSkipListener.onSkipInProcess : " + item);
		System.out.println("CustomSkipListener.onSkipInProcess : " + t.getMessage());
	}
}
