package com.moresby.ed.stockportfolio.config;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.trade.model.pojo.TradePOJO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public Faker faker(){
        return new Faker();
    }

    @Bean
    public TradePOJO tradePOJO(){
        return new TradePOJO();
    }
}
