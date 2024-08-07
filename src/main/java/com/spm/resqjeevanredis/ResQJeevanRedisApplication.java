package com.spm.resqjeevanredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ResQJeevanRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResQJeevanRedisApplication.class, args);
	}

}
