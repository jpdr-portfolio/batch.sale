package com.challenge.acc.batch.sale.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleCsvDto {
  
  Long id;
  Integer pointOfSale;
  BigDecimal amount;
  Integer quantity;
  Integer temperature;
  Integer customerId;
  UUID productId;
  
}
