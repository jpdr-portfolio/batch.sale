package com.challenge.acc.batch.sale.job.loader.tasklet;

import com.challenge.acc.batch.sale.model.SaleMaster;
import com.challenge.acc.batch.sale.repository.SalesMasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Map;

import static com.challenge.acc.batch.sale.util.Constants.CSV_SALES_FILE_PARAM_NAME;
import static com.challenge.acc.batch.sale.util.TestDataUtils.TEST_FILE_NAME;
import static com.challenge.acc.batch.sale.util.TestDataUtils.getTestSaleMaster;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvLoaderMasterCreatorTaskletTest {
  
  @Mock
  private SalesMasterRepository repository;
  @Mock
  private StepContribution contribution;
  @Mock
  private ChunkContext chunkContext;
  @Mock
  private StepContext stepContext;
  @Mock
  private StepExecution stepExecution;
  @Mock
  private JobExecution jobExecution;
  @Mock
  private ExecutionContext executionContext;
  
  @InjectMocks
  private CsvLoaderMasterCreatorTasklet tasklet;
  
  @BeforeEach
  void beforeEach(){
    when(this.chunkContext.getStepContext()).thenReturn(this.stepContext);
  }
  
  @Test
  @DisplayName("OK - Master Record is created")
  void givenFileNameWhenExecuteThenReturnCompleted() throws Exception {
    
    when(this.jobExecution.getExecutionContext()).thenReturn(this.executionContext);
    when(this.stepExecution.getJobExecution()).thenReturn(this.jobExecution);
    when(this.stepContext.getStepExecution()).thenReturn(this.stepExecution);
    
    SaleMaster saleMaster = getTestSaleMaster("PENDING");
    Map<String, Object> jobParameters = Map.of(CSV_SALES_FILE_PARAM_NAME, TEST_FILE_NAME);
    when(this.stepContext.getJobParameters()).thenReturn(jobParameters);
    when(this.repository.save(any(SaleMaster.class))).thenReturn(saleMaster);
    
    RepeatStatus resultStatus = tasklet.execute(this.contribution, this.chunkContext);
    
    assertEquals(RepeatStatus.FINISHED, resultStatus);
    
  }
  
  @Test
  @DisplayName("ERROR - File not present in context")
  void givenFileNameWhenExecuteThenThrowException(){
    
    Map<String, Object> jobParameters = Map.of();
    
    when(this.stepContext.getJobParameters()).thenReturn(jobParameters);
    
    assertThrows(JobExecutionException.class, () ->
      tasklet.execute(this.contribution, this.chunkContext));
    
  }
  
  @Test
  @DisplayName("ERROR - Sale Master Save returns null id")
  void givenNullSaleMasterWhenExecuteThenThrowException(){
    
    Map<String, Object> jobParameters = Map.of(CSV_SALES_FILE_PARAM_NAME, TEST_FILE_NAME);
    when(this.stepContext.getJobParameters()).thenReturn(jobParameters);
    SaleMaster saleMaster = getTestSaleMaster("PENDING");
    saleMaster.setMasterId(null);
    when(this.repository.save(any(SaleMaster.class))).thenReturn(saleMaster);

    assertThrows(JobExecutionException.class, () ->
      tasklet.execute(this.contribution, this.chunkContext));
    
  }
  
}
