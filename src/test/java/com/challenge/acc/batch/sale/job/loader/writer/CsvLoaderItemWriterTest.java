package com.challenge.acc.batch.sale.job.loader.writer;

import com.challenge.acc.batch.sale.model.SaleDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.batch.item.Chunk;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static com.challenge.acc.batch.sale.util.TestDataUtils.getTestSaleChunk;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvLoaderItemWriterTest {
  
  @Mock
  private DataSource dataSource;
  @Mock
  private Connection connection;
  @Mock
  private PreparedStatement stmt;
  @InjectMocks
  private CsvLoaderItemWriter itemWriter;
  
  @BeforeEach
  void beforeEach() throws Exception{
    when(this.dataSource.getConnection()).thenReturn(this.connection);
    when(this.connection.prepareStatement(anyString())).thenReturn(stmt);
  }

  @Test
  @DisplayName("OK - Chunk with null sales")
  void givenChunkWithNullSalesWhenWriteThenReturnVoid(){
    assertDoesNotThrow(() -> itemWriter.write(null));
  }
  
  @Test
  @DisplayName("OK - Chunk with empty sales")
  void givenChunkWithEmptySalesWhenWriteThenReturnVoid(){
    Chunk<SaleDetails> chunk = Chunk.of();
    assertDoesNotThrow(() -> itemWriter.write(chunk));
  }
  
  @Test
  @DisplayName("OK - Chunk with one sale")
  void givenChunkWithOneSaleWhenWriteThenReturnVoid() throws Exception{
    Chunk<SaleDetails> chunk = getTestSaleChunk(1);
    itemWriter.write(chunk);
    verify(connection, times(9)).createArrayOf(anyString(), any());
    verify(stmt, times(9)).setArray(anyInt(), any());
  }
  
  @Test
  @DisplayName("OK - Chunk with three sales")
  void givenChunkWithThreeSalesWhenWriteThenReturnVoid() throws Exception{
    Chunk<SaleDetails> chunk = getTestSaleChunk(3);
    itemWriter.write(chunk);
    verify(connection, times(9)).createArrayOf(anyString(), any());
    verify(stmt, times(9)).setArray(anyInt(), any());
  }
 
  
}
