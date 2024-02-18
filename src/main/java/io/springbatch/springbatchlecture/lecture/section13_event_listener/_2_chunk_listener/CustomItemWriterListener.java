package io.springbatch.springbatchlecture.lecture.section13_event_listener._2_chunk_listener;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class CustomItemWriterListener implements ItemWriteListener<String> {

	@Override
	public void beforeWrite(Chunk<? extends String> items) {
		System.out.println("CustomItemWriterListener.beforeWrite");
	}

	@Override
	public void afterWrite(Chunk<? extends String> items) {
		System.out.println("CustomItemWriterListener.afterWrite");
	}

	@Override
	public void onWriteError(Exception exception, Chunk<? extends String> items) {
		System.out.println("CustomItemWriterListener.onWriteError");
	}
}
