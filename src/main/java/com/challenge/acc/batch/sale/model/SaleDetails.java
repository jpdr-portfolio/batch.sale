package com.challenge.acc.batch.sale.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SaleDetails {
  
  Integer masterId;
  Long detailsId;
  Integer pointOfSale;
  BigDecimal amount;
  Integer quantity;
  BigDecimal taxes;
  Integer customerId;
  UUID productId;
  OffsetDateTime creationTimestamp;

}
