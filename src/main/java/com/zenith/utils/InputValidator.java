package com.zenith.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.zenith.exception.InvalidRequestParameters;

public class InputValidator {

	public static void validateCreateMatchParameters(String team1, String team2, String startTime, double duration)
			throws InvalidRequestParameters {
		validateTeamName(team1);
		validateTeamName(team2);
		vaidateStartTime(startTime);
		validateDuration(duration);
	}

	public static void validateUpdateScore(String id, String team)
			throws InvalidRequestParameters {
		validateMatchId(id);
		validateTeamName(team);
	}

	public static void validateGetMatch(String id)
			throws InvalidRequestParameters {
		validateMatchId(id);
	}

	public static void validateGetScore(String id)
			throws InvalidRequestParameters {
		validateMatchId(id);
	}
	
	private static void validateDuration(double duration) throws InvalidRequestParameters {
		if(duration<=0) 
			throw new InvalidRequestParameters("Duration should be greater than 0");
	}

	private static void vaidateStartTime(String startTime) throws InvalidRequestParameters {
		if(startTime.equals(""))
			throw new InvalidRequestParameters("Start Time should not be empty");
		validateDateFormat(startTime);
		
		Duration duration = Duration.between( LocalDateTime.now(), LocalDateTime.parse(startTime));
		if(duration.isNegative()) 
			throw new InvalidRequestParameters("Star time should be a future DateTime");
	}

	private static void validateDateFormat(String startTime) throws InvalidRequestParameters {
		try {
			LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}catch (DateTimeParseException e) {
			throw new InvalidRequestParameters("Please Enter a Valid Date");
        }		
	}

	private static void validateTeamName(String teamName) throws InvalidRequestParameters {
		if (teamName.length() < 1)
			throw new InvalidRequestParameters("Please enter valid team name");
	}
	
	private static void validateMatchId(String id) throws InvalidRequestParameters {
		if (id.length() < 1)
			throw new InvalidRequestParameters("Please enter valid Match id");
	}
}
