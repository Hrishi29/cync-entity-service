package com.idexcel.cync.los.entity.dto;

/**
 * The Enum ErrorResponseCode.
 */
public enum ErrorResponseCode {

	/** The resource already exist. */
	RESOURCE_ALREADY_EXIST,

	/** The resource already used. */
	RESOURCE_ALREADY_USED,

	/** The resource not found. */
	RESOURCE_NOT_FOUND,

	/** invalid request */
	INVALID_REQUEST,

	/** Bad request **/
	BAD_REQUEST,

	/** internal server error in case of any other exception */
	INTERNAL_SERVER_ERROR,

	/** when FS validation fails */
	PRECONDITION_FAILED, NOT_FOUND,

	CONFLICT;
}
