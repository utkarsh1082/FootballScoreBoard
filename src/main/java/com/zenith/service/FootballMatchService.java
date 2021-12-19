package com.zenith.service;

import java.util.concurrent.ExecutionException;

import com.zenith.exception.CustomErrorException;
import com.zenith.model.FootballMatch;
import com.zenith.model.Score;

public interface FootballMatchService {
	
	public FootballMatch saveMatch(String team1, String team2, String startTime, double duration) throws InterruptedException, ExecutionException, CustomErrorException;

	public FootballMatch getMatch(String matchId) throws InterruptedException, ExecutionException, CustomErrorException;

	public Score getScore(String matchId) throws InterruptedException, ExecutionException, CustomErrorException;
	
	public Score updateScore(String matchId, String team) throws InterruptedException, ExecutionException, CustomErrorException;
}
