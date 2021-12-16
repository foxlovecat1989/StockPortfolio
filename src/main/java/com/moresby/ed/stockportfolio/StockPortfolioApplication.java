package com.moresby.ed.stockportfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

import static com.moresby.ed.stockportfolio.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class StockPortfolioApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockPortfolioApplication.class, args);
		new File(USER_FOLDER).mkdirs();
	}
}
