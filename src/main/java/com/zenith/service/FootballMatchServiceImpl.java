package com.zenith.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import com.zenith.dao.FootballMatchDao;
import com.zenith.exception.CustomErrorException;
import com.zenith.model.FootballMatch;
import com.zenith.model.Score;

@Service
public class FootballMatchServiceImpl implements FootballMatchService {

	private final FootballMatchDao footballDao;

	public FootballMatchServiceImpl(FootballMatchDao footballDao) {
		this.footballDao = footballDao;
	}

	@Override
	public FootballMatch saveMatch(String team1, String team2, String startTime, double duration)
			throws InterruptedException, ExecutionException, CustomErrorException {

		FootballMatch footballMatch = createNewMatch(team1, team2, startTime, duration);
		return footballDao.saveMatch(footballMatch);
	}

	@Override
	public FootballMatch getMatch(String matchId)
			throws InterruptedException, ExecutionException, CustomErrorException {
		FootballMatch footballMatch = footballDao.getMatch(matchId);
		if (footballMatch.isMatchEnded() == false
				&& getTimeLeft(footballMatch.getStartTime(), footballMatch.getDuration()) <= 0) {
			footballMatch.setMatchEnded(true);
		}
		return footballMatch;
	}

	@Override
	public Score updateScore(String matchId, String team)
			throws InterruptedException, ExecutionException, CustomErrorException {

		FootballMatch footballMatch = getMatch(matchId);
		if (footballMatch.isMatchEnded())
			throw new CustomErrorException("Match has ended");

		if (!(footballMatch.getTeam1().equals(team) || footballMatch.getTeam2().equals(team)))
			throw new CustomErrorException("Invalid team name");

		double duration = getTimeLeft(footballMatch.getStartTime(), footballMatch.getDuration());

		if (duration<0) {
			footballMatch.setMatchEnded(true);
			throw new CustomErrorException("Match has ended");
		}

		if (getTimeLeft(footballMatch.getStartTime(), footballMatch.getDuration()) > 0) {
				if (footballMatch.getTeam1().equals(team))
					footballMatch.setScore1(footballMatch.getScore1() + 1);
				else if (footballMatch.getTeam2().equals(team))
					footballMatch.setScore2(footballMatch.getScore2() + 1);
		} else {
			footballMatch.setMatchEnded(true);
		}
		footballMatch = footballDao.saveMatch(footballMatch);
		return getScoreFromMatch(footballMatch);
	}

	public Score getScore(String matchId) throws InterruptedException, ExecutionException, CustomErrorException {
		return getScoreFromMatch(footballDao.getMatch(matchId));
	}

	private Score getScoreFromMatch(FootballMatch footballMatch) {
		Score score = new Score();
		score.setMatchId(footballMatch.getId());
		score.setScore1(footballMatch.getScore1());
		score.setScore2(footballMatch.getScore2());
		score.setTeam1(footballMatch.getTeam1());
		score.setTeam2(footballMatch.getTeam2());
		score.setTimeLeft(getTimeLeft(footballMatch.getStartTime(), footballMatch.getDuration()));
		return score;
	}

	private double getTimeLeft(String startTime, double matchDuration) {
		LocalDateTime endDateTime = LocalDateTime.parse(startTime).plusMinutes((long) matchDuration);
		Duration duration = Duration.between(LocalDateTime.now(), endDateTime);
		long minutes = duration.getSeconds() / 60;
		long seconds = duration.getSeconds() % 60;
		double timeLeft = minutes + Math.round(seconds * 100.0) / 10000.0;
		if (timeLeft < 0)
			return 0;
		return timeLeft;
	}

	private FootballMatch createNewMatch(String team1, String team2, String startTime, double duration) {
		FootballMatch footballMatch = new FootballMatch();
		footballMatch.setId(team1 + team2 + LocalDate.now().toString());
		footballMatch.setTeam1(team1);
		footballMatch.setTeam2(team2);
		footballMatch.setScore1(0);
		footballMatch.setScore2(0);
		footballMatch.setStartTime(startTime);
		footballMatch.setDuration(duration);
		return footballMatch;
	}

}
