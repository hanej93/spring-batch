package io.springbatch.springbatchlecture.lecture.section13_event_listener._3_skip_listener;

public class CustomSkipException extends RuntimeException {

	public CustomSkipException() {
		super();
	}

	public CustomSkipException(String s) {
		super(s);
	}
}
