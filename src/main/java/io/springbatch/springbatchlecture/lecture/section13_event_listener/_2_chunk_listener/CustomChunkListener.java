package io.springbatch.springbatchlecture.lecture.section13_event_listener._2_chunk_listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

public class CustomChunkListener {

	@BeforeChunk
	public void beforeChunk(ChunkContext context) {
		System.out.println("CustomChunkListener.beforeChunk");
	}

	@AfterChunk
	public void afterChunk(ChunkContext context) {
		System.out.println("CustomChunkListener.afterChunk");
	}

	@AfterChunkError
	public void afterChunkError(ChunkContext context) {
		System.out.println("CustomChunkListener.afterChunkError");
	}


}
