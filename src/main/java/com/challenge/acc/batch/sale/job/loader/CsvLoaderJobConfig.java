package com.challenge.acc.batch.sale.job.loader;

import com.challenge.acc.batch.sale.dto.SaleCsvDto;
import com.challenge.acc.batch.sale.filter.CsvLoaderChunkListener;
import com.challenge.acc.batch.sale.model.Sale;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;

import static com.challenge.acc.batch.sale.util.Constants.CHUNK_SIZE;
import static com.challenge.acc.batch.sale.util.Constants.CSV_SALES_FILE_PARAM_NAME;

@Configuration
public class CsvLoaderJobConfig {
  
  @Bean
  public Job csvLoaderJob(JobRepository jobRepository, Step csvLoaderStep){
    JobBuilder builder = new JobBuilder("csvLoaderJob", jobRepository);
    return builder
      .incrementer(new RunIdIncrementer())
      .start(csvLoaderStep)
      .build();
  }
  
  @Bean
  public JobParametersValidator csvLoaderJobParametersValidator(){
    return jobParameters -> {
      if(jobParameters == null || jobParameters.getParameter(CSV_SALES_FILE_PARAM_NAME) == null ||
        Objects.requireNonNull(jobParameters.getString(CSV_SALES_FILE_PARAM_NAME)).isEmpty()){
        throw new JobParametersInvalidException("The required parameter '" + CSV_SALES_FILE_PARAM_NAME +
        "' wasn't found in command line parameters.");
      }
    };
  }
  
  @Bean
  public Step csvLoaderStep(JobRepository jobRepository,
    PlatformTransactionManager platformTransactionManager, ItemReader<SaleCsvDto> saleItemReader,
      ItemProcessor<SaleCsvDto, Sale> saleItemProcessor, ItemWriter<Sale> saleItemWriter,
        TaskExecutor taskExecutor, CsvLoaderChunkListener csvLoaderChunkListener){
    StepBuilder builder = new StepBuilder("csvLoaderStep", jobRepository);
    return builder.<SaleCsvDto, Sale> chunk(CHUNK_SIZE, platformTransactionManager)
      .reader(saleItemReader)
      .processor(saleItemProcessor)
      .writer(saleItemWriter)
      .taskExecutor(taskExecutor)
      .listener(csvLoaderChunkListener)
      .build();
  }
  
  @Bean
  public TaskExecutor taskExecutor(){
    return new SimpleAsyncTaskExecutor("batchSaleTaskExecutor");
  }
  
}
