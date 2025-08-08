package com.challenge.acc.batch.sale.util;

import com.challenge.acc.batch.sale.dto.SaleCsvDto;
import com.challenge.acc.batch.sale.model.Sale;
import org.springframework.batch.item.Chunk;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDataUtils {
  
  private static final UUID PRODUCT_ID = UUID
    .fromString("e0451b3e-235c-4d65-ba09-8875eb3bd84c");
  
  private static final String TIMESTAMP_VALUE =
    "2025-08-07T16:11:06.675684100-03:00";
  
  private TestDataUtils(){}
  
  
  public static Chunk<Sale> getTestSaleChunk(int items){
    Sale[] sales = new Sale[items];
    for(int i = 0; i < items; i++) {
      sales[i] = getTestSale();
    }
    return Chunk.of(sales);
  }
  
  public static SaleCsvDto getTestSaleCsvDto(){
    return SaleCsvDto.builder()
      .pointOfSale(1)
      .amount(BigDecimal.ONE)
      .quantity(Integer.MAX_VALUE)
      .temperature(25)
      .customerId(Integer.MAX_VALUE)
      .productId(PRODUCT_ID)
      .build();
  }
  
  public static Sale getTestSale(){
    return Sale.builder()
      .id(null)
      .pointOfSale(1)
      .quantity(1)
      .amount(BigDecimal.valueOf(100.00))
      .taxes(BigDecimal.valueOf(21.00))
      .productId(PRODUCT_ID)
      .customerId(1)
      .creationTimestamp(OffsetDateTime.parse(TIMESTAMP_VALUE))
      .build();
  }

  
  public static void assertSale(SaleCsvDto saleCsvDto, Sale sale){
    assertEquals(saleCsvDto.getPointOfSale(), sale.getPointOfSale());
    assertEquals(saleCsvDto.getAmount(), sale.getAmount());
    assertEquals(saleCsvDto.getQuantity(), sale.getQuantity());
    assertEquals(saleCsvDto.getCustomerId(), sale.getCustomerId());
    assertEquals(saleCsvDto.getProductId(), sale.getProductId());
    assertNotNull(sale.getTaxes());
    assertNotNull(sale.getCreationTimestamp());
  }
  
}
