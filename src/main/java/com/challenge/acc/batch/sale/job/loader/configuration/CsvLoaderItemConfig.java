package com.challenge.acc.batch.sale.job.loader.configuration;

import com.challenge.acc.batch.sale.dto.SaleCsvDto;
import com.challenge.acc.batch.sale.dto.TaxDto;
import com.challenge.acc.batch.sale.job.loader.processor.CsvLoaderItemProcessor;
import com.challenge.acc.batch.sale.job.loader.writer.CsvLoaderItemWriter;
import com.challenge.acc.batch.sale.model.SaleDetails;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.Map;

import static com.challenge.acc.batch.sale.util.Constants.CSV_SALES_FILE_PARAM_NAME;
import static com.challenge.acc.batch.sale.util.Constants.CSV_SALE_ITEM_NAMES;

@Configuration
public class CsvLoaderItemConfig {
  
  @Bean
  @StepScope
  public FlatFileItemReader<SaleCsvDto> cvsSaleItemReader(
    @Value("#{jobParameters['" + CSV_SALES_FILE_PARAM_NAME + "']}") String csvSalesFileParamName) {
    FileSystemResource cvsSalesFileSystemResource = new FileSystemResource(csvSalesFileParamName);
    if (!cvsSalesFileSystemResource.exists()) {
      throw new IllegalArgumentException("The CSV input file " +
        csvSalesFileParamName + " wasn't found.");
    }
    FlatFileItemReaderBuilder<SaleCsvDto> builder = new FlatFileItemReaderBuilder<>();
    return builder.name("cvsSaleItemReader")
      .resource(cvsSalesFileSystemResource)
      .linesToSkip(1)
      .delimited()
      .names(CSV_SALE_ITEM_NAMES)
      .targetType(SaleCsvDto.class)
      .build();
  }
  
  @Bean
  public ItemProcessor<SaleCsvDto, SaleDetails> csvLoaderItemProcessor(Map<Integer, TaxDto> provincesTaxesMap) {
    return new CsvLoaderItemProcessor(provincesTaxesMap);
  }
  
  @Bean
  public ItemWriter<SaleDetails> csvLoaderItemWriter(DataSource dataSource) {
    return new CsvLoaderItemWriter(dataSource);
  }
  
}
