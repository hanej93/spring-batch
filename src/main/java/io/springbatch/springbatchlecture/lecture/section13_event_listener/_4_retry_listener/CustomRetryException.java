package io.springbatch.springbatchlecture.lecture.section13_event_listener._4_retry_listener;

public class CustomRetryException extends RuntimeException {

	public CustomRetryException() {
		super();
	}

	public CustomRetryException(String s) {
		super(s);
	}
}
