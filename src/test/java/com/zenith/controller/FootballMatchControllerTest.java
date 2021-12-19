package com.zenith.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zenith.exception.CustomErrorException;
import com.zenith.model.FootballMatch;
import com.zenith.model.Score;
import com.zenith.service.FootballMatchService;

public class FootballMatchControllerTest {

	private FootballMatchController footballMatchController;
	private FootballMatchService footballMatchService;
	private Score score1;

	@Before
	public void setUp() throws Exception {
		footballMatchService = mock(FootballMatchService.class);
		footballMatchController = new FootballMatchController(footballMatchService);
		score1 = new Score();
		score1.setScore1(1);
		score1.setScore2(2);
		score1.setMatchId("match1");
		score1.setTeam1("Team1");
		score1.setTeam2("Team2");
	}

	@Test
	public void saveMatchForInvalidTeamName1() throws InterruptedException, ExecutionException {
		String team1 = "";
		String team2 = "team2";
		String start = "2022-12-19T22:00:00";
		double duration = 90;
		ResponseEntity<FootballMatch> response = footballMatchController.saveMatch(team1, team2, start, duration);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void saveMatchForInvalidTeamName2() throws InterruptedException, ExecutionException {
		String team1 = "team1";
		String team2 = "";
		String start = "2022-12-19T22:00:00";
		double duration = 90;
		ResponseEntity<FootballMatch> response = footballMatchController.saveMatch(team1, team2, start, duration);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void saveMatchForInvalidDateFormat() throws InterruptedException, ExecutionException {
		String team1 = "team1";
		String team2 = "team2";
		String start = "2022-1922:00:00";
		double duration = 90;
		ResponseEntity<FootballMatch> response = footballMatchController.saveMatch(team1, team2, start, duration);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void saveMatchForNullStartTime() throws InterruptedException, ExecutionException {
		String team1 = "team1";
		String team2 = "team2";
		String start = null;
		double duration = 90;
		ResponseEntity<FootballMatch> response = footballMatchController.saveMatch(team1, team2, start, duration);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void saveMatchForPastDate() throws InterruptedException, ExecutionException {
		String team1 = "team1";
		String team2 = "team2";
		String start = "2012-12-19T22:00:00";
		double duration = 0;
		ResponseEntity<FootballMatch> response = footballMatchController.saveMatch(team1, team2, start, duration);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void saveMatchForInvaidDuration() throws InterruptedException, ExecutionException {
		String team1 = "team1";
		String team2 = "team2";
		String start = "2022-12-19T22:00:00";
		double duration = 0;
		ResponseEntity<FootballMatch> response = footballMatchController.saveMatch(team1, team2, start, duration);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void saveMatchForValidArgs() throws InterruptedException, ExecutionException {
		String team1 = "team1";
		String team2 = "team2";
		String start = "2022-12-19T22:00:00";
		double duration = 90;
		ResponseEntity<FootballMatch> response = footballMatchController.saveMatch(team1, team2, start, duration);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void saveMatchWithErrorForMatchCreation() throws InterruptedException, ExecutionException {
		String team1 = "team1";
		String team2 = "team2";
		String start = "2022-12-19T22:00:00";
		double duration = 90;
		try {
			when(footballMatchService.saveMatch(any(), any(), any(),anyDouble())).thenThrow(new CustomErrorException("Match couldn't be saved"));
		} catch (InterruptedException | ExecutionException | CustomErrorException e) {
			e.printStackTrace();
		}
		ResponseEntity<FootballMatch> response = footballMatchController.saveMatch(team1, team2, start, duration);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void updateScoreForInvalidId() throws InterruptedException, ExecutionException {
		String id = "";
		String team = "team";
		ResponseEntity<Score> response = footballMatchController.updateScore(id, team);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void updateScoreForInvalidTeamName() throws InterruptedException, ExecutionException {
		String id = "id";
		String team = "";
		ResponseEntity<Score> response = footballMatchController.updateScore(id, team);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void updateScoreForInvalidArgs() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "id";
		String team = "team";
		when(footballMatchService.updateScore(any(), any())).thenReturn(score1);
		ResponseEntity<Score> response = footballMatchController.updateScore(id, team);
		assertEquals(score1, response.getBody());
	}
	@Test
	public void updateScoreForTeamNameNotFound() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "id";
		String team = "team";
		when(footballMatchService.updateScore(any(), any())).thenThrow(new CustomErrorException("Invalid team name"));
		ResponseEntity<Score> response = footballMatchController.updateScore(id, team);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void getScoreForInvalidId() throws InterruptedException, ExecutionException {
		String id = "";
		ResponseEntity<Score> response = footballMatchController.getScore(id);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void getScore() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "id";
		when(footballMatchService.getScore(any())).thenReturn(score1);
		ResponseEntity<Score> response = footballMatchController.getScore(id);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void getScoreForTeamNameNotFound() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "id";
		when(footballMatchService.getScore(any())).thenThrow(new CustomErrorException("No match found with this id"));
		ResponseEntity<Score> response = footballMatchController.getScore(id);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	@Test
	public void getMatchForInvalidId() throws InterruptedException, ExecutionException {
		String id = "";
		ResponseEntity<FootballMatch> response = footballMatchController.getMatch(id);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void getMatch() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "id";
		when(footballMatchService.getScore(any())).thenReturn(score1);
		ResponseEntity<FootballMatch> response = footballMatchController.getMatch(id);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void getMatchForTeamNameNotFound() throws InterruptedException, ExecutionException, CustomErrorException {
		String id = "id";
		when(footballMatchService.getMatch(any())).thenThrow(new CustomErrorException("No match found with this id"));
		ResponseEntity<FootballMatch> response = footballMatchController.getMatch(id);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
}
