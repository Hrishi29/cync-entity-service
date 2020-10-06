package com.idexcel.cync.los.entity.exception;

/**
 * Exception Handler class for LOS Entity
 * 
 * @author Idexcel
 *
 */

public class LosEntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LosEntityNotFoundException(String message) {
		super(message);
	}

}
