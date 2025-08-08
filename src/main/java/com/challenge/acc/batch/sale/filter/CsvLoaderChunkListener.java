package com.challenge.acc.batch.sale.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CsvLoaderChunkListener implements ChunkListener {
  
  @Override
  public void beforeChunk(@NonNull ChunkContext context) {
    ChunkListener.super.beforeChunk(context);
    log.debug("Chunk started on thread " + Thread.currentThread() );
  }
  
  @Override
  public void afterChunk(@NonNull ChunkContext context) {
    ChunkListener.super.afterChunk(context);
    log.debug("Chunk finished on thread " + Thread.currentThread());
    
  }
  
  @Override
  public void afterChunkError(@NonNull ChunkContext context) {
    ChunkListener.super.afterChunkError(context);
    log.warn("Chunk error on thread " + Thread.currentThread());
  }
}
