package com.challenge.acc.batch.sale.util;

import com.challenge.acc.batch.sale.dto.SaleCsvDto;
import com.challenge.acc.batch.sale.model.SaleDetails;
import com.challenge.acc.batch.sale.model.SaleMaster;
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
  
  public static final String TEST_FILE_NAME = "file.csv";
  
  private TestDataUtils(){}
  
  
  public static Chunk<SaleDetails> getTestSaleChunk(int items){
    SaleDetails[] saleDetails = new SaleDetails[items];
    for(int i = 0; i < items; i++) {
      saleDetails[i] = getTestSale(i+1);
    }
    return Chunk.of(saleDetails);
  }
  
  public static SaleCsvDto getTestSaleCsvDto(){
    return SaleCsvDto.builder()
      .id(1L)
      .pointOfSale(1)
      .amount(BigDecimal.ONE)
      .quantity(Integer.MAX_VALUE)
      .temperature(25)
      .customerId(Integer.MAX_VALUE)
      .productId(PRODUCT_ID)
      .build();
  }
  
  public static SaleDetails getTestSale(long detailsId){
    return SaleDetails.builder()
      .masterId(1)
      .detailsId(detailsId)
      .pointOfSale(1)
      .quantity(1)
      .amount(BigDecimal.valueOf(100.00))
      .taxes(BigDecimal.valueOf(21.00))
      .productId(PRODUCT_ID)
      .customerId(1)
      .creationTimestamp(OffsetDateTime.parse(TIMESTAMP_VALUE))
      .build();
  }
  
  public static SaleMaster getTestSaleMaster(String status){
    return SaleMaster.builder()
      .masterId(1)
      .fileName(TEST_FILE_NAME)
      .creationTimestamp(OffsetDateTime.parse(TIMESTAMP_VALUE))
      .status(status)
      .updateTimestamp(null)
      .build();
  }

  
  public static void assertSale(SaleCsvDto saleCsvDto, SaleDetails saleDetails){
    assertEquals(saleCsvDto.getPointOfSale(), saleDetails.getPointOfSale());
    assertEquals(saleCsvDto.getAmount(), saleDetails.getAmount());
    assertEquals(saleCsvDto.getQuantity(), saleDetails.getQuantity());
    assertEquals(saleCsvDto.getCustomerId(), saleDetails.getCustomerId());
    assertEquals(saleCsvDto.getProductId(), saleDetails.getProductId());
    assertNotNull(saleDetails.getTaxes());
    assertNotNull(saleDetails.getCreationTimestamp());
  }
  
}
