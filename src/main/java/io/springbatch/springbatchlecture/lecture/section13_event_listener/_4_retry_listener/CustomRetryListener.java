package io.springbatch.springbatchlecture.lecture.section13_event_listener._4_retry_listener;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class CustomRetryListener implements RetryListener {

	@Override
	public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
		System.out.println("CustomRetryListener.open");
		return true;
	}

	@Override
	public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
		System.out.println("CustomRetryListener.close");
	}

	@Override
	public <T, E extends Throwable> void onSuccess(RetryContext context, RetryCallback<T, E> callback, T result) {
		System.out.println("CustomRetryListener.onSuccess");
	}

	@Override
	public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
		System.out.println("CustomRetryListener.onError Retry Count : " + context.getRetryCount());
	}
}
