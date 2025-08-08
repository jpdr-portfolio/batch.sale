package com.challenge.acc.batch.sale.job.loader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.challenge.acc.batch.sale.util.Constants.CSV_SALES_FILE_PARAM_NAME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CsvLoaderJobConfigTest {
  
  @Autowired
  private JobParametersValidator jobParametersValidator;
  
  @Test
  @DisplayName("OK - JobParametersValidator correct file")
  void givenOkJobParameterWhenValidateThenReturnVoid(){
    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    jobParametersBuilder.addString(CSV_SALES_FILE_PARAM_NAME, "file.csv");
    assertDoesNotThrow(() ->
      this.jobParametersValidator.validate(jobParametersBuilder.toJobParameters()));
  }

  @Test
  @DisplayName("ERROR - JobParametersValidator null parameters")
  void givenNullJobParametersWhenValidateThenThrowException(){
    assertThrows(JobParametersInvalidException.class, () ->
      this.jobParametersValidator.validate(null));
  }
  
  @Test
  @DisplayName("ERROR - JobParametersValidator missing parameter")
  void givenMissingParameterWhenValidateThenThrowException(){
    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    assertThrows(JobParametersInvalidException.class, () ->
      this.jobParametersValidator.validate(jobParametersBuilder.toJobParameters()));
  }
  
}
