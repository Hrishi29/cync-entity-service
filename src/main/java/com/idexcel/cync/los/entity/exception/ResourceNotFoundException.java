package com.idexcel.cync.los.entity.exception;

/**
 * Custom Exception Class for Resource Not Found
 * 
 * @author Idexcel
 *
 */
public class ResourceNotFoundException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new resource not found exception.
	 *
	 * @param message the message
	 */
	public ResourceNotFoundException(final String message) {
		super(message);
	}

}
