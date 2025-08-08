package com.challenge.acc.batch.sale.job.loader.writer;

import com.challenge.acc.batch.sale.model.SaleDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_BIGINT;
import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_INTEGER;
import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_NUMERIC;
import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_TIMESTAMP;
import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_UUID;
import static com.challenge.acc.batch.sale.util.Constants.SQL_SALE_INSERT_QUERY_UNNEST;

@RequiredArgsConstructor
public class CsvLoaderItemWriter implements ItemWriter<SaleDetails> {
  
  private final DataSource dataSource;
  private final DateTimeFormatter formatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSXXX");
  
  @Override
  public void write(Chunk<? extends SaleDetails> chunk) throws Exception {
    if(chunk == null || chunk.isEmpty()){
      return;
    }
    List<SaleDetails> saleDetailsList = new ArrayList<>(chunk.getItems());
    if (!saleDetailsList.isEmpty()) {
      
      Integer[] masterIds = new Integer[saleDetailsList.size()];
      Long[] detailsIds = new Long[saleDetailsList.size()];
      Integer[] pointOfSales = new Integer[saleDetailsList.size()];
      BigDecimal[] amounts = new BigDecimal[saleDetailsList.size()];
      Integer[] quantities = new Integer[saleDetailsList.size()];
      BigDecimal[] taxes = new BigDecimal[saleDetailsList.size()];
      Integer[] customerIds = new Integer[saleDetailsList.size()];
      UUID[] productIds =  new UUID[saleDetailsList.size()];
      String[] creationTimestamps = new String[saleDetailsList.size()];
      
      for(int i = 0; i < saleDetailsList.size(); i++){
        SaleDetails saleDetails = saleDetailsList.get(i);
        masterIds[i] = saleDetails.getMasterId();
        detailsIds[i] = saleDetails.getDetailsId();
        pointOfSales[i] = saleDetails.getPointOfSale();
        amounts[i] = saleDetails.getAmount();
        quantities[i] = saleDetails.getQuantity();
        taxes[i] = saleDetails.getTaxes();
        customerIds[i] = saleDetails.getCustomerId();
        productIds[i] = saleDetails.getProductId();
        creationTimestamps[i] = saleDetails.getCreationTimestamp().format(formatter);
      }
      
      try (Connection conn = dataSource.getConnection();
           PreparedStatement stmt = conn.prepareStatement(SQL_SALE_INSERT_QUERY_UNNEST)) {
        
        Array masterIdsArray = conn.createArrayOf(POSTGRES_INTEGER, masterIds);
        Array detailsIdsArray = conn.createArrayOf(POSTGRES_BIGINT, detailsIds);
        Array pointOfSalesArray = conn.createArrayOf(POSTGRES_INTEGER, pointOfSales);
        Array amountsArray = conn.createArrayOf(POSTGRES_NUMERIC, amounts);
        Array quantitiesArray = conn.createArrayOf(POSTGRES_INTEGER, quantities);
        Array taxesArray = conn.createArrayOf(POSTGRES_NUMERIC, taxes);
        Array customerIdsArray = conn.createArrayOf(POSTGRES_INTEGER, customerIds);
        Array productIdsArray = conn.createArrayOf(POSTGRES_UUID, productIds);
        Array creationTimestampsArray = conn.createArrayOf(POSTGRES_TIMESTAMP, creationTimestamps);
        
        stmt.setArray(1, masterIdsArray);
        stmt.setArray(2, detailsIdsArray);
        stmt.setArray(3, pointOfSalesArray);
        stmt.setArray(4, amountsArray);
        stmt.setArray(5, quantitiesArray);
        stmt.setArray(6, taxesArray);
        stmt.setArray(7, customerIdsArray);
        stmt.setArray(8, productIdsArray);
        stmt.setArray(9, creationTimestampsArray);
        
        stmt.executeUpdate();
      }
    }
  }
}
