package com.challenge.acc.batch.sale.job.loader.writer;

import com.challenge.acc.batch.sale.model.Sale;
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

import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_INTEGER;
import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_NUMERIC;
import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_TIMESTAMP;
import static com.challenge.acc.batch.sale.util.Constants.POSTGRES_UUID;
import static com.challenge.acc.batch.sale.util.Constants.SQL_SALE_INSERT_QUERY_UNNEST;

@RequiredArgsConstructor
public class CsvLoaderItemWriter implements ItemWriter<Sale> {
  
  private final DataSource dataSource;
  private final DateTimeFormatter formatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSXXX");
  
  @Override
  public void write(Chunk<? extends Sale> chunk) throws Exception {
    if(chunk == null || chunk.isEmpty()){
      return;
    }
    List<Sale> sales = new ArrayList<>(chunk.getItems());
    if (!sales.isEmpty()) {
      
      Integer[] pointOfSales = new Integer[sales.size()];
      BigDecimal[] amounts = new BigDecimal[sales.size()];
      Integer[] quantities = new Integer[sales.size()];
      BigDecimal[] taxes = new BigDecimal[sales.size()];
      Integer[] customerIds = new Integer[sales.size()];
      UUID[] productIds =  new UUID[sales.size()];
      String[] creationTimestamps = new String[sales.size()];
      
      for(int i = 0; i < sales.size(); i++){
        Sale sale = sales.get(i);
        pointOfSales[i] = sale.getPointOfSale();
        amounts[i] = sale.getAmount();
        quantities[i] = sale.getQuantity();
        taxes[i] = sale.getTaxes();
        customerIds[i] = sale.getCustomerId();
        productIds[i] = sale.getProductId();
        creationTimestamps[i] = sale.getCreationTimestamp().format(formatter);
      }
      
      try (Connection conn = dataSource.getConnection();
           PreparedStatement stmt = conn.prepareStatement(SQL_SALE_INSERT_QUERY_UNNEST)) {
        
        Array pointOfSalesArray = conn.createArrayOf(POSTGRES_INTEGER, pointOfSales);
        Array amountsArray = conn.createArrayOf(POSTGRES_NUMERIC, amounts);
        Array quantitiesArray = conn.createArrayOf(POSTGRES_INTEGER, quantities);
        Array taxesArray = conn.createArrayOf(POSTGRES_NUMERIC, taxes);
        Array customerIdsArray = conn.createArrayOf(POSTGRES_INTEGER, customerIds);
        Array productIdsArray = conn.createArrayOf(POSTGRES_UUID, productIds);
        Array creationTimestampsArray = conn.createArrayOf(POSTGRES_TIMESTAMP, creationTimestamps);
        
        stmt.setArray(1, pointOfSalesArray);
        stmt.setArray(2, amountsArray);
        stmt.setArray(3, quantitiesArray);
        stmt.setArray(4, taxesArray);
        stmt.setArray(5, customerIdsArray);
        stmt.setArray(6, productIdsArray);
        stmt.setArray(7, creationTimestampsArray);
        
        stmt.executeUpdate();
      }
    }
  }
}
