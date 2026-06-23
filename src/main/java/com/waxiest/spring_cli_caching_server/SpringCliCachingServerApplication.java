package com.waxiest.spring_cli_caching_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringCliCachingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCliCachingServerApplication.class, args);
	}

}
