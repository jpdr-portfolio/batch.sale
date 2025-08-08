package com.challenge.acc.batch.sale.job.loader.tasklet;

import com.challenge.acc.batch.sale.model.SaleMaster;
import com.challenge.acc.batch.sale.repository.SalesMasterRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

import static com.challenge.acc.batch.sale.util.Constants.CSV_SALES_FILE_PARAM_MISSING_MESSAGE;
import static com.challenge.acc.batch.sale.util.Constants.CSV_SALES_FILE_PARAM_NAME;

@Component
@RequiredArgsConstructor
public class CsvLoaderMasterCreatorTasklet implements Tasklet {
  
  private final SalesMasterRepository salesMasterRepository;
  
  @Override
  public RepeatStatus execute(@NonNull StepContribution contribution, ChunkContext chunkContext)
    throws Exception {
    Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
    if(!jobParameters.containsKey(CSV_SALES_FILE_PARAM_NAME) ||
      !(jobParameters.get(CSV_SALES_FILE_PARAM_NAME) instanceof String)){
      throw new JobExecutionException(CSV_SALES_FILE_PARAM_MISSING_MESSAGE);
    }
    String fileName = (String) jobParameters.get(CSV_SALES_FILE_PARAM_NAME);
    OffsetDateTime creationTimestamp = OffsetDateTime.now();
    SaleMaster newSaleMaster = SaleMaster.builder()
      .masterId(null)
      .fileName(fileName)
      .creationTimestamp(creationTimestamp)
      .status("PENDING")
      .updateTimestamp(null)
      .build();
    
    SaleMaster savedSaleMaster = this.salesMasterRepository.save(newSaleMaster);
    if(Objects.requireNonNull(savedSaleMaster).getMasterId() == null){
      throw new JobExecutionException("There was an error while creating the new sales master.");
    }
    
    ExecutionContext executionContext = chunkContext.getStepContext()
      .getStepExecution()
      .getJobExecution()
      .getExecutionContext();

    executionContext.putInt("masterId", savedSaleMaster.getMasterId());
    
    return RepeatStatus.FINISHED;
  }
}
