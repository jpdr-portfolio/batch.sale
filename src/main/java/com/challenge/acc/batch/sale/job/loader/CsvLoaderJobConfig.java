package com.challenge.acc.batch.sale.job.loader;

import com.challenge.acc.batch.sale.dto.SaleCsvDto;
import com.challenge.acc.batch.sale.job.loader.tasklet.CsvLoaderMasterCreatorTasklet;
import com.challenge.acc.batch.sale.job.loader.tasklet.CsvLoaderMasterUpdaterTasklet;
import com.challenge.acc.batch.sale.model.SaleDetails;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
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
import static com.challenge.acc.batch.sale.util.Constants.CSV_SALES_FILE_PARAM_MISSING_MESSAGE;
import static com.challenge.acc.batch.sale.util.Constants.CSV_SALES_FILE_PARAM_NAME;

@Configuration
public class CsvLoaderJobConfig {
  
  @Bean
  public Job csvLoaderJob(JobRepository jobRepository,
    Step csvLoaderMasterCreatorStep,
    Step csvLoaderDetailsStep,
    Step csvLoaderMasterUpdaterStep){
    JobBuilder builder = new JobBuilder("csvLoaderJob", jobRepository);
    return builder
      .incrementer(new RunIdIncrementer())
      .start(csvLoaderMasterCreatorStep)
      .next(csvLoaderDetailsStep)
      .next(csvLoaderMasterUpdaterStep)
      .build();
  }
  
  @Bean
  public JobParametersValidator csvLoaderJobParametersValidator(){
    return jobParameters -> {
      if(jobParameters == null || jobParameters.getParameter(CSV_SALES_FILE_PARAM_NAME) == null ||
        Objects.requireNonNull(jobParameters.getString(CSV_SALES_FILE_PARAM_NAME)).isEmpty()){
        throw new JobParametersInvalidException(CSV_SALES_FILE_PARAM_MISSING_MESSAGE);
      }
    };
  }
  
  @Bean(name = "csvLoaderMasterCreatorStep")
  public Step csvLoaderMasterCreatorStep(JobRepository jobRepository,
    CsvLoaderMasterCreatorTasklet tasklet, PlatformTransactionManager platformTransactionManager){
    TaskletStepBuilder taskletStepBuilder =
      new StepBuilder("csvLoaderMasterCreatorStep", jobRepository)
        .tasklet(tasklet, platformTransactionManager);
    return taskletStepBuilder
      .allowStartIfComplete(false)
      .build();
  }
  
  @Bean(name = "csvLoaderDetailsStep")
  public Step csvLoaderDetailsStep(JobRepository jobRepository,
    PlatformTransactionManager platformTransactionManager, ItemReader<SaleCsvDto> saleItemReader,
      ItemProcessor<SaleCsvDto, SaleDetails> saleItemProcessor, ItemWriter<SaleDetails> saleItemWriter,
        TaskExecutor taskExecutor){
    StepBuilder builder = new StepBuilder("csvLoaderStep", jobRepository);
    return builder.<SaleCsvDto, SaleDetails> chunk(CHUNK_SIZE, platformTransactionManager)
      .reader(saleItemReader)
      .processor(saleItemProcessor)
      .writer(saleItemWriter)
      .taskExecutor(taskExecutor)
      .build();
  }
  
  @Bean(name = "csvLoaderMasterUpdaterStep")
  public Step csvLoaderMasterUpdaterStep(JobRepository jobRepository,
    CsvLoaderMasterUpdaterTasklet tasklet, PlatformTransactionManager platformTransactionManager){
    TaskletStepBuilder taskletStepBuilder =
      new StepBuilder("csvLoaderMasterUpdaterStep", jobRepository)
        .tasklet(tasklet, platformTransactionManager);
    return taskletStepBuilder
      .allowStartIfComplete(false)
      .build();
  }
  
  @Bean
  public TaskExecutor taskExecutor(){
    return new SimpleAsyncTaskExecutor("batchSaleTaskExecutor");
  }
  
}
