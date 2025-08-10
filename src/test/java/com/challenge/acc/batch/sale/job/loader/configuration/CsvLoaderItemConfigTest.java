package com.challenge.acc.batch.sale.job.loader.configuration;

import com.challenge.acc.batch.sale.dto.SaleCsvDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CsvLoaderItemConfigTest {
  
  @InjectMocks
  private CsvLoaderItemConfig loaderItemConfig;
  @InjectMocks
  private FlatFileItemReader<SaleCsvDto> itemReader;
  
  @Test
  @DisplayName("OK - ItemReader Initialization - File Exists")
  void givenNonEmptyFileResourceWhenInitReaderBeanThenReturnBean(@TempDir Path tempDir)
    throws IOException {
    Path testFile =
      Files.createFile(tempDir.resolve("" + System.currentTimeMillis()));
    assertNotNull(loaderItemConfig.cvsSaleItemReader(testFile.toString()));
  }

  @Test
  @DisplayName("ERROR - ItemReader Initialization - File Doesn't Exist")
  void givenNullFileResourceWhenInitReaderBeanThenThrowException(){
    assertThrows(IllegalArgumentException.class, () ->
      loaderItemConfig.cvsSaleItemReader("wrongFile.csv"));
  }
  
}
