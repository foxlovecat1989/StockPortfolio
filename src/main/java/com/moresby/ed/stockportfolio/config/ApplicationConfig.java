package com.moresby.ed.stockportfolio.config;

import com.github.javafaker.Faker;
import com.moresby.ed.stockportfolio.domain.CustomEmail;
import com.moresby.ed.stockportfolio.domain.TradePOJO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Bean
    public CustomEmail customEmail(){
        return new CustomEmail();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
