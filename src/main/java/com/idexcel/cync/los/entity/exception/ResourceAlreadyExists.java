package com.idexcel.cync.los.entity.exception;

/**
 * Custom Exception Class for Resource Already Exists
 * 
 * @author ssaakshi
 *
 */
public class ResourceAlreadyExists extends RuntimeException {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new resource already exists exception
	 *
	 * @param message the message
	 */
	public ResourceAlreadyExists(final String message) {
		super(message);
	}
}
