package com.idexcel.cync.los.entity.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

/**
 * Model Dto Class for Error Response
 * 
 * @author Idexcel
 *
 */
@Getter
public class ErrorDto {
	/** The timestamp. */
	private final String timestamp;

	/** The code. */
	private final ErrorResponseCode code;

	/** The message. */
	private final String message;

	/** The path. */
	private final String path;

	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Instantiates a new error dto.
	 *
	 * @param code    the code
	 * @param message the message
	 * @param path    the path
	 */
	public ErrorDto(ErrorResponseCode code, String message, String path) {
		this.code = code;
		this.message = message;
		this.path = path;

		LocalDateTime now = LocalDateTime.now();
		this.timestamp = now.format(DateTimeFormatter.ofPattern(DATE_PATTERN));

	}
}
