package io.springbatch.springbatchlecture.lecture.section11_repeat_error._3_skip;

public class NonSkippableException extends Exception {
	public NonSkippableException(String s) {
		super(s);
	}
}
