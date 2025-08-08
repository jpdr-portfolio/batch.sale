package com.challenge.acc.batch.sale.configuration;

import com.challenge.acc.batch.sale.dto.TaxDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static com.challenge.acc.batch.sale.util.Constants.PROVINCE_NAMES;

@Configuration
public class InMemoryDataConfig {
  
  @Bean
  public Map<Integer, TaxDto> provincesTaxesMap(){
    Map<Integer, TaxDto> provincesTaxesMap = new HashMap<>();
    for(int i = 0; i < PROVINCE_NAMES.length; i++){
      int key = i+1;
      BigDecimal taxes = BigDecimal.valueOf(key).divide(BigDecimal.valueOf(100),4, RoundingMode.UP);
      provincesTaxesMap.put(key, new TaxDto(PROVINCE_NAMES[i], taxes));
    }
    return provincesTaxesMap;
  }
  
}
