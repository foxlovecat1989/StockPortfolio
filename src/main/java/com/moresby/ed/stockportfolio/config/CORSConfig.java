package com.moresby.ed.stockportfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

	@Bean
	public CorsFilter corsFilter(){
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Authorization", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin Accept", "X-Requested-With", "XMLHttpRequest",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-token", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", corsConfiguration);

		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/api/**")
//				.allowedMethods("GET","POST","PUT","DELETE","OPTIONS", "PATCH")
//				.allowedHeaders("*")
//				.allowedOrigins("http://localhost:4200")
//				.allowCredentials(true);
//
//		//TODO: Need to change the URL for the production URL when we deploy
//	}
}