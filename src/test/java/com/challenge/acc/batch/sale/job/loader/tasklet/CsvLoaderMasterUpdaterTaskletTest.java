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

import java.util.Optional;

import static com.challenge.acc.batch.sale.util.TestDataUtils.getTestSaleMaster;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvLoaderMasterUpdaterTaskletTest {
  
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
  private CsvLoaderMasterUpdaterTasklet tasklet;
  
  @BeforeEach
  void beforeEach(){
    when(this.chunkContext.getStepContext()).thenReturn(this.stepContext);
  }
  
  @Test
  @DisplayName("OK - Sale Master is Updated")
  void givenSaleMasterWhenExecuteThenReturnCompleted() throws Exception{
    
    when(this.executionContext.containsKey(anyString())).thenReturn(true);
    when(this.jobExecution.getExecutionContext()).thenReturn(this.executionContext);
    when(this.stepExecution.getJobExecution()).thenReturn(this.jobExecution);
    when(this.stepContext.getStepExecution()).thenReturn(this.stepExecution);
    
    SaleMaster currentSaleMaster = getTestSaleMaster("PENDING");
    when(this.repository.findById(anyInt())).thenReturn(Optional.of(currentSaleMaster));
    
    RepeatStatus resultStatus = tasklet.execute(this.contribution, this.chunkContext);
    
    assertEquals(RepeatStatus.FINISHED, resultStatus);
    
  }
  
  @Test
  @DisplayName("ERROR - Sale Master Id not found")
  void givenSaleMasterNotFoundWhenExecuteThenThrowException(){
    
    when(this.jobExecution.getExecutionContext()).thenReturn(this.executionContext);
    when(this.stepExecution.getJobExecution()).thenReturn(this.jobExecution);
    when(this.stepContext.getStepExecution()).thenReturn(this.stepExecution);
    
    assertThrows(JobExecutionException.class, () ->
      tasklet.execute(this.contribution, this.chunkContext));
    
  }
  
}
