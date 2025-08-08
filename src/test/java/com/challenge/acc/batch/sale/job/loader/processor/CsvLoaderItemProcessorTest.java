package com.challenge.acc.batch.sale.job.loader.processor;

import com.challenge.acc.batch.sale.dto.SaleCsvDto;
import com.challenge.acc.batch.sale.dto.TaxDto;
import com.challenge.acc.batch.sale.model.SaleDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Map;

import static com.challenge.acc.batch.sale.util.TestDataUtils.assertSale;
import static com.challenge.acc.batch.sale.util.TestDataUtils.getTestSaleCsvDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvLoaderItemProcessorTest {
  
  @Autowired
  private Map<Integer, TaxDto> provincesTaxesMap;
  
  @Autowired
  private CsvLoaderItemProcessor itemProcessor;
  
  @Test
  @DisplayName("OK - One Sale")
  void givenSaleCSVWhenProcessThenReturnSale(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    SaleDetails resultSaleDetails = itemProcessor.process(inputSale);
    assertNotNull(resultSaleDetails);
    assertSale(inputSale, resultSaleDetails);
  }
  
  @Test
  @DisplayName("ERROR - Invalid pointOfSale")
  void givenSaleCSVWhenInvalidPointOfSaleThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setPointOfSale(-1);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  @Test
  @DisplayName("ERROR - Invalid null pointOfSale")
  void givenSaleCSVWhenNullPointOfSaleThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setPointOfSale(null);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  @Test
  @DisplayName("ERROR - Invalid amount")
  void givenSaleCSVWhenInvalidAmountThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setAmount(BigDecimal.ZERO);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  @Test
  @DisplayName("ERROR - Invalid null amount")
  void givenSaleCSVWhenNullAmountThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setAmount(null);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  @Test
  @DisplayName("ERROR - Invalid quantity")
  void givenSaleCSVWhenInvalidQuantityThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setQuantity(0);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }

  @Test
  @DisplayName("ERROR - Invalid null quantity")
  void givenSaleCSVWhenNullQuantityThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setQuantity(null);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  @Test
  @DisplayName("ERROR - Invalid temperature")
  void givenSaleCSVWhenInvalidTemperatureThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setTemperature(-100);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  
  @Test
  @DisplayName("ERROR - Invalid null temperature")
  void givenSaleCSVWhenNullTemperatureThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setTemperature(null);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  @Test
  @DisplayName("ERROR - Invalid customerId")
  void givenSaleCSVWhenInvalidCustomerIdThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setCustomerId(-1);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  
  @Test
  @DisplayName("ERROR - Invalid null customerId")
  void givenSaleCSVWhenNullCustomerIdThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setCustomerId(null);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  
  @Test
  @DisplayName("ERROR - Invalid productId")
  void givenSaleCSVWhenInvalidProductIdThenThrowException(){
    SaleCsvDto inputSale = getTestSaleCsvDto();
    inputSale.setProductId(null);
    
    assertThrows(ValidationException.class, () ->
      itemProcessor.process(inputSale));
  }
  

  
  @Test
  @DisplayName("OK - Sales Master Id is present.")
  void givenSalesMasterIdWhenBeforeStepThenReturnVoid(){
    StepContext stepContext = mock(StepContext.class);
    StepExecution stepExecution = mock(StepExecution.class);
    JobExecution jobExecution = mock(JobExecution.class);
    ExecutionContext executionContext = mock(ExecutionContext.class);
    when(executionContext.containsKey(anyString())).thenReturn(true);
    when(executionContext.getInt(anyString())).thenReturn(1);
    when(jobExecution.getExecutionContext()).thenReturn(executionContext);
    when(stepExecution.getJobExecution()).thenReturn(jobExecution);
    when(stepContext.getStepExecution()).thenReturn(stepExecution);
    assertDoesNotThrow(() -> itemProcessor.beforeStep(stepExecution));
  }
  
  
  @Test
  @DisplayName("ERROR - Sales Master Id is not present.")
  void givenSalesMasterIdNotFoundWhenBeforeStepThenThrowException(){
    StepContext stepContext = mock(StepContext.class);
    StepExecution stepExecution = mock(StepExecution.class);
    JobExecution jobExecution = mock(JobExecution.class);
    ExecutionContext executionContext = mock(ExecutionContext.class);
    when(executionContext.containsKey(anyString())).thenReturn(false);
    when(jobExecution.getExecutionContext()).thenReturn(executionContext);
    when(stepExecution.getJobExecution()).thenReturn(jobExecution);
    when(stepContext.getStepExecution()).thenReturn(stepExecution);
    assertThrows(ValidationException.class, () ->
      itemProcessor.beforeStep(stepExecution));
  }
  
}
