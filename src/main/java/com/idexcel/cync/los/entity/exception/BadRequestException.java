package com.idexcel.cync.los.entity.exception;

/**
 * The class which will handle all the bad request . when clients sends invalid/
 * improper request
 * 
 * @author Idexcel
 *
 */
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Overriden constructor for bad request
	 * 
	 * @param message
	 */
	public BadRequestException(final String message) {
		super(message);
	}

}