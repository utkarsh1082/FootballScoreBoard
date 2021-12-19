package com.zenith.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.LocalDateTime;

import com.zenith.exception.CustomErrorException;
import com.zenith.exception.InvalidRequestParameters;
import com.zenith.model.FootballMatch;
import com.zenith.model.Score;
import com.zenith.service.FootballMatchService;
import com.zenith.utils.InputValidator;

@RestController
@RequestMapping("/football")
public class FootballMatchController {

	private final int ADD_SECONDS = 10;
	private final FootballMatchService footballMatchService;

	public FootballMatchController(FootballMatchService footballMatchService) {
		this.footballMatchService = footballMatchService;
	}

	@PostMapping(value = "/match")
	public ResponseEntity<FootballMatch> saveMatch(@RequestParam(value = "team1", required = true) String team1,
			@RequestParam(value = "team2", required = true) String team2,
			@RequestParam(value = "start", required = false) String startTime,
			@RequestParam(value = "duration", required = true) double duration)
			throws InterruptedException, ExecutionException {

		FootballMatch footballMatch;

		try {
			if(startTime == null)	
				startTime = LocalDateTime.now().plusSeconds(ADD_SECONDS).toString();
			InputValidator.validateCreateMatchParameters(team1, team2, startTime, duration);
		} catch (InvalidRequestParameters e) {
			footballMatch = new FootballMatch();
			footballMatch.setErrorDescription(e.getMessage());
			return new ResponseEntity<FootballMatch>(footballMatch, HttpStatus.BAD_REQUEST);
		}
		
		try {
			footballMatch = footballMatchService.saveMatch(team1, team2, startTime, duration);
			return new ResponseEntity<FootballMatch>(footballMatch, HttpStatus.CREATED);
		} catch (InterruptedException | ExecutionException | CustomErrorException e) {
			footballMatch = new FootballMatch();
			footballMatch.setErrorDescription(e.getMessage());			
			return new ResponseEntity<FootballMatch>(footballMatch, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/score")
	public ResponseEntity<Score> updateScore(@RequestParam(value = "id", required = true) String matchId,
			@RequestParam(value = "team", required = true) String team)
			throws InterruptedException, ExecutionException {

		Score score;

		try {
			InputValidator.validateUpdateScore(matchId, team);
		} catch (InvalidRequestParameters e) {
			score = new Score();
			score.setErrorDescription(e.getMessage());
			return new ResponseEntity<Score>(score, HttpStatus.BAD_REQUEST);
		}

		try {
			score = footballMatchService.updateScore(matchId, team);
		}
		catch (CustomErrorException e) {
			score = new Score();
			score.setErrorDescription(e.getMessage());
			if(score.getErrorDescription().equals("Invalid team name"))
				return new ResponseEntity<Score>(score, HttpStatus.BAD_REQUEST);
			return new ResponseEntity<Score>(score, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Score>(score, HttpStatus.OK);
	}

	@GetMapping(value = "/match", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FootballMatch> getMatch(@RequestParam(value = "id") String id)
			throws InterruptedException, ExecutionException {
		FootballMatch footballMatch;
		try {
			InputValidator.validateGetMatch(id);
		} catch (InvalidRequestParameters e) {
			footballMatch = new FootballMatch();
			footballMatch.setErrorDescription(e.getMessage());
			return new ResponseEntity<FootballMatch>(footballMatch, HttpStatus.BAD_REQUEST);
		}

		try {
			footballMatch = footballMatchService.getMatch(id);
		} catch (InterruptedException | ExecutionException | CustomErrorException e) {
			footballMatch = new FootballMatch();
			footballMatch.setErrorDescription(e.getMessage());
			return new ResponseEntity<FootballMatch>(footballMatch, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<FootballMatch>(footballMatch, HttpStatus.OK);
	}

	@GetMapping(value = "/score", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Score> getScore(@RequestParam(value = "id") String id)
			throws InterruptedException, ExecutionException {
		Score score;

		try {
			InputValidator.validateGetScore(id);
		} catch (InvalidRequestParameters e) {
			score = new Score();
			score.setErrorDescription(e.getMessage());
			return new ResponseEntity<Score>(score, HttpStatus.BAD_REQUEST);
		}

		try {
			score = footballMatchService.getScore(id);
		} catch (InterruptedException | ExecutionException | CustomErrorException e) {
			score = new Score();
			score.setErrorDescription(e.getMessage());
			return new ResponseEntity<Score>(score, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Score>(score, HttpStatus.OK);
	}

}
