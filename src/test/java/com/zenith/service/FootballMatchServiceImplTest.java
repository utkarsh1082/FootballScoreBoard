package com.zenith.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.zenith.dao.FootballMatchDao;
import com.zenith.exception.CustomErrorException;
import com.zenith.model.FootballMatch;
import com.zenith.model.Score;

public class FootballMatchServiceImplTest {
	private FootballMatchDao footballMatchDao;
	private FootballMatchService footballMatchService;
	private FootballMatch footballMatch1;
	private FootballMatch footballMatch2;
	private FootballMatch footballMatch3;
	private Score score1;
	
	@Before
	public void setUp() throws Exception {
		footballMatchDao = mock(FootballMatchDao.class);
		footballMatchService = new FootballMatchServiceImpl(footballMatchDao);
		
		footballMatch1 = new FootballMatch();
		footballMatch1.setId("match1");
		footballMatch1.setScore1(1);
		footballMatch1.setScore2(2);
		footballMatch1.setTeam1("Team1");
		footballMatch1.setTeam2("Team2");
		footballMatch1.setStartTime("2022-12-19T22:00:00");
		
		footballMatch2 = new FootballMatch();
		footballMatch2.setId("match2");
		footballMatch2.setScore1(1);
		footballMatch2.setScore2(2);
		footballMatch2.setTeam1("Team11");
		footballMatch2.setTeam2("Team21");
		footballMatch2.setStartTime("2022-12-19T22:00:00");
		footballMatch2.setMatchEnded(true);
		
		footballMatch3 = new FootballMatch();
		footballMatch3.setId("match3");
		footballMatch3.setScore1(4);
		footballMatch3.setScore2(3);
		footballMatch3.setTeam1("Team13");
		footballMatch3.setTeam2("Team23");
		footballMatch3.setStartTime("2012-12-19T22:00:00");
		footballMatch3.setMatchEnded(false);
		
		score1 = new Score();
		score1.setScore1(1);
		score1.setScore2(2);
		score1.setMatchId("match1");
		score1.setTeam1("Team1");
		score1.setTeam2("Team2");
	}

	@Test
	public void saveMatch() throws InterruptedException, ExecutionException, CustomErrorException {
		String team1 = "team1";
		String team2 = "team2";
		String start = "2022-12-19T22:00:00";
		double duration = 90;
		try {
			when(footballMatchDao.saveMatch(any())).thenReturn(footballMatch1);
		} catch (InterruptedException | ExecutionException | CustomErrorException e) {
			e.printStackTrace();
		}
		FootballMatch footballMatch = footballMatchService.saveMatch(team1, team2, start, duration);
		assertEquals("match1", footballMatch.getId());
	}

	@Test
	public void getMatch() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "match1";
		try {
			when(footballMatchDao.getMatch(any())).thenReturn(footballMatch1);
		} catch (InterruptedException | ExecutionException | CustomErrorException e) {
			e.printStackTrace();
		}
		FootballMatch footballMatch = footballMatchService.getMatch(id);
		assertEquals("match1", footballMatch.getId());
	}

	@Test
	public void getMatchForMatchEnded() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "match1";
		try {
			when(footballMatchDao.getMatch(any())).thenReturn(footballMatch2);
		} catch (InterruptedException | ExecutionException | CustomErrorException e) {
			e.printStackTrace();
		}
		FootballMatch footballMatch = footballMatchService.getMatch(id);
		assertEquals("match2", footballMatch.getId());
	}

	@Test
	public void getScore() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "match1";
		try {
			when(footballMatchDao.getMatch(any())).thenReturn(footballMatch1);
		} catch (InterruptedException | ExecutionException | CustomErrorException e) {
			e.printStackTrace();
		}
		Score score = footballMatchService.getScore(id);
		assertEquals("match1", score.getMatchId());
	}

	@Test
	public void updateScoreForTeam1() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "match1";
		String team = "Team1";
		when(footballMatchDao.getMatch(any())).thenReturn(footballMatch1);
		when(footballMatchDao.saveMatch(any())).thenReturn(footballMatch1);
		Score score = footballMatchService.updateScore(id, team);
		assertEquals(2, score.getScore1());
	}
	
	@Test
	public void updateScoreForTeam2() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "match1";
		String team = "Team2";
		when(footballMatchDao.getMatch(any())).thenReturn(footballMatch1);
		when(footballMatchDao.saveMatch(any())).thenReturn(footballMatch1);
		Score score = footballMatchService.updateScore(id, team);
		assertEquals(3, score.getScore2());
	}

	@Test(expected=CustomErrorException.class)
	public void updateScoreMatchEnded() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "match1";
		String team = "Team1";
		when(footballMatchDao.getMatch(any())).thenReturn(footballMatch2);
		footballMatchService.updateScore(id, team);
	}
	@Test(expected=CustomErrorException.class)
	public void updateScoreForInvalidTeamName() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "match1";
		String team = "Team3";
		when(footballMatchDao.getMatch(any())).thenReturn(footballMatch1);
		footballMatchService.updateScore(id, team);
	}
	@Test(expected=CustomErrorException.class)
	public void updateScoreForEndedMatch() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "match1";
		String team = "Team2";
		when(footballMatchDao.getMatch(any())).thenReturn(footballMatch3);
		footballMatchService.updateScore(id, team);
	}
}
