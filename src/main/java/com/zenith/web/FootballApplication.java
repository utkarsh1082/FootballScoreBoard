package com.zenith.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.zenith.config.ServiceConfig;

@SpringBootApplication(scanBasePackageClasses = {ServiceConfig.class})
@ComponentScan("com.zenith")
public class FootballApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballApplication.class, args);
	}

}
