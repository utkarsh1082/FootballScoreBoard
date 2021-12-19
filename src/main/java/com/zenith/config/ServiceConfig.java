package com.zenith.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zenith.dao.FootballMatchDao;
import com.zenith.dao.FootballMatchDaoImpl;

@Configuration
public class ServiceConfig {

	@Bean
	public FootballMatchDao getFootballMatchDao() {
		return new FootballMatchDaoImpl();
	}
}
