package com.zenith.dao;

import java.util.concurrent.ExecutionException;

import com.zenith.exception.CustomErrorException;
import com.zenith.model.FootballMatch;

public interface FootballMatchDao {
	public FootballMatch saveMatch(FootballMatch footballMatch) throws InterruptedException, ExecutionException, CustomErrorException;
	public FootballMatch getMatch(String matchId) throws InterruptedException, ExecutionException, CustomErrorException;
}
