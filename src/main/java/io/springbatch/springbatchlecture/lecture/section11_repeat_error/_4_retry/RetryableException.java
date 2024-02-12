package io.springbatch.springbatchlecture.lecture.section11_repeat_error._4_retry;

public class RetryableException extends RuntimeException {

	public RetryableException() {
		super();
	}

	public RetryableException(String message) {
		super(message);
	}
}
