package com.challenge.acc.batch.sale.job.loader.tasklet;

import com.challenge.acc.batch.sale.model.SaleMaster;
import com.challenge.acc.batch.sale.repository.SalesMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class CsvLoaderMasterUpdaterTasklet implements Tasklet {
  
  private final SalesMasterRepository salesMasterRepository;
  
  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    
    ExecutionContext executionContext = chunkContext.getStepContext()
      .getStepExecution()
      .getJobExecution()
      .getExecutionContext();

    if(!executionContext.containsKey("masterId")){
      throw new JobExecutionException("Expected 'masterId' context variable is missing.");
    }
    Integer masterId = executionContext.getInt("masterId");
    OffsetDateTime updateTimestamp = OffsetDateTime.now();
    
    SaleMaster updatedSaleMaster = this.salesMasterRepository.findById(masterId)
      .orElseThrow(() -> new JobExecutionException("The required sales master with id " +
        masterId + " wasn't found."));
    
    updatedSaleMaster.setStatus("COMPLETED");
    updatedSaleMaster.setUpdateTimestamp(updateTimestamp);
    
    this.salesMasterRepository.save(updatedSaleMaster);
    
    return RepeatStatus.FINISHED;
  }
}
