package com.challenge.acc.batch.sale.job.loader.processor;

import com.challenge.acc.batch.sale.dto.SaleCsvDto;
import com.challenge.acc.batch.sale.dto.TaxDto;
import com.challenge.acc.batch.sale.model.SaleDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

import static com.challenge.acc.batch.sale.util.Constants.MAX_TEMPERATURE;
import static com.challenge.acc.batch.sale.util.Constants.MIN_TEMPERATURE;

@RequiredArgsConstructor
public class CsvLoaderItemProcessor implements ItemProcessor<SaleCsvDto, SaleDetails>,
  StepExecutionListener {
  
  private final Map<Integer, TaxDto> provincesTaxesMap;
  private Integer masterId;
  
  @Override
  public void beforeStep(StepExecution stepExecution) {
    ExecutionContext executionContext = stepExecution
        .getJobExecution()
        .getExecutionContext();
    if(!executionContext.containsKey("masterId")){
      throw new ValidationException("Expected 'masterId' context variable is missing.");
    }
    this.masterId = executionContext.getInt("masterId");
  }

  
  @Override
  public SaleDetails process(@NonNull SaleCsvDto saleCsvDto){
    validateSaleCsvDto(saleCsvDto);
    BigDecimal taxes = calculateTaxes(saleCsvDto.getPointOfSale(),saleCsvDto.getAmount());
    OffsetDateTime creationTimestamp = OffsetDateTime.now();
    return SaleDetails.builder()
      .masterId(this.masterId)
      .detailsId(saleCsvDto.getId())
      .pointOfSale(saleCsvDto.getPointOfSale())
      .amount(saleCsvDto.getAmount())
      .quantity(saleCsvDto.getQuantity())
      .taxes(taxes)
      .customerId(saleCsvDto.getCustomerId())
      .productId(saleCsvDto.getProductId())
      .creationTimestamp(creationTimestamp)
      .build();
  }
  
  private BigDecimal calculateTaxes(Integer pointOfSale, BigDecimal amount){
    TaxDto provinceTaxes = this.provincesTaxesMap.get(pointOfSale);
    return amount.multiply(provinceTaxes.getTaxAmount())
      .setScale(2, RoundingMode.UP);
  }
  
  private void validateSaleCsvDto(SaleCsvDto saleCsvDto){
    
    if(saleCsvDto.getId() == null){
      throw new ValidationException("Invalid id: null");
    }
    
    if(saleCsvDto.getPointOfSale() == null ||
      !this.provincesTaxesMap.containsKey(saleCsvDto.getPointOfSale())){
      throw new ValidationException("Invalid pointOfSale: " +
        Objects.requireNonNullElse(saleCsvDto.getPointOfSale(), "null"));
    }
    if(saleCsvDto.getAmount() == null || saleCsvDto.getAmount().compareTo(BigDecimal.ZERO) <= 0){
      throw new ValidationException("Invalid amount: " +
        Objects.requireNonNullElse(saleCsvDto.getAmount(), "null"));
    }
    if(saleCsvDto.getQuantity() == null || saleCsvDto.getQuantity() < 1){
      throw new ValidationException("Invalid quantity: " +
        Objects.requireNonNullElse(saleCsvDto.getQuantity(), "null"));
    }
    if(saleCsvDto.getTemperature() == null ||
      saleCsvDto.getTemperature() < MIN_TEMPERATURE || saleCsvDto.getTemperature() > MAX_TEMPERATURE){
      throw new ValidationException("Invalid temperature: " +
        Objects.requireNonNullElse(saleCsvDto.getTemperature(), "null") );
    }
    if(saleCsvDto.getCustomerId() == null || saleCsvDto.getCustomerId() < 1){
      throw new ValidationException("Invalid customerId: " +
        Objects.requireNonNullElse(saleCsvDto.getCustomerId(), "null") );
    }
    if(saleCsvDto.getProductId() == null){
      throw new ValidationException("Invalid productId: null");
    }
    
  }
  
}
