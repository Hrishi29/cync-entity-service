package com.idexcel.cync.los.entity.exception;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ERROR_INVALID_FORMAT_EXCEPTION;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.ImmutableMap;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.dto.ErrorDto;
import com.idexcel.cync.los.entity.dto.ErrorResponseCode;

/**
 * Exception Handler class for Los Entity apis
 * 
 * @author Idexcel
 *
 */
@ControllerAdvice
public class LosEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	MessageSource errorMsgs;

	private static final String URI = "uri=";
	private static final String ERROR = "error";

	/**
	 * Handle bad request exception.
	 * 
	 * @return {@link HttpStatus} 400
	 */
	@ExceptionHandler({ BadRequestException.class })
	public ResponseEntity<ImmutableMap<String, ErrorDto>> handleBadRequestException(HttpServletRequest request,
			Exception ex) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorResponseCode.BAD_REQUEST, ex, request);
	}

	/**
	 * Handle resource not found exception.
	 * 
	 * @param request the request
	 * @param ex      the exception object
	 * @return {@link HttpStatus} 404
	 */
	@ExceptionHandler({ ResourceNotFoundException.class })
	public ResponseEntity<ImmutableMap<String, ErrorDto>> handleResourceNotFoundException(HttpServletRequest request,
			Exception ex) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, ErrorResponseCode.RESOURCE_NOT_FOUND, ex, request);
	}

	/**
	 * Handle LosEntity not found exception.
	 * 
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler({ LosEntityNotFoundException.class })
	public ResponseEntity<ImmutableMap<String, ErrorDto>> handleEntityNotFoundException(HttpServletRequest request,
			Exception ex) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, ErrorResponseCode.NOT_FOUND, ex, request);
	}

	/**
	 * Handle Conflict exception.
	 * 
	 * @param request
	 * @param ex
	 * @return{@link HttpStatus} 409
	 */
	@ExceptionHandler({ ResourceAlreadyExists.class })
	public ResponseEntity<ImmutableMap<String, ErrorDto>> handleResourceAlreadyExistsException(
			HttpServletRequest request, Exception ex) {
		return buildErrorResponse(HttpStatus.CONFLICT, ErrorResponseCode.RESOURCE_ALREADY_EXIST, ex, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable throwable = ex.getCause();
		if (throwable instanceof InvalidFormatException) {
			InvalidFormatException formatException = (InvalidFormatException) throwable;
			String fieldName = "";
			for (JsonMappingException.Reference r : formatException.getPath()) {
				fieldName = r.getFieldName();
			}
			String errMsg = errorMsgs.getMessage(ERROR_INVALID_FORMAT_EXCEPTION,
					new Object[] { formatException.getValue(), fieldName }, CommonUtils.getLocale());
			ErrorDto errorDto = new ErrorDto(ErrorResponseCode.INVALID_REQUEST, errMsg,
					request.getDescription(false).replace(URI, ""));
			ImmutableMap<String, ErrorDto> responseVal = ImmutableMap.of(ERROR, errorDto);
			return new ResponseEntity<>(responseVal, headers, status);
		}
		return buildErrorResponseCode(HttpStatus.BAD_REQUEST, ErrorResponseCode.BAD_REQUEST, ex);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		ErrorDto errorDto = new ErrorDto(ErrorResponseCode.BAD_REQUEST, error,
				request.getDescription(false).replace(URI, ""));
		ImmutableMap<String, ErrorDto> responseVal = ImmutableMap.of(ERROR, errorDto);
		return new ResponseEntity<>(responseVal, headers, status);
	}

	private ResponseEntity<Object> buildErrorResponseCode(HttpStatus httpStatus, ErrorResponseCode errorResponseCode,
			Exception ex) {
		String msg = ex.getLocalizedMessage();
		String[] errorMessage = msg.split("\\n");

		String path = MDC.get("requestURI");
		ErrorDto errorDto = new ErrorDto(errorResponseCode, errorMessage[0], path);
		ImmutableMap<String, ErrorDto> responseVal = ImmutableMap.of(ERROR, errorDto);
		return ResponseEntity.status(httpStatus).body(responseVal);
	}

	/**
	 * Builds the error response.
	 *
	 * @param httpStatus        the http status
	 * @param errorResponseCode the error response code
	 * @param ex                the exception object
	 * @param request           the request
	 * @return the response entity
	 */
	private ResponseEntity<ImmutableMap<String, ErrorDto>> buildErrorResponse(HttpStatus httpStatus,
			ErrorResponseCode errorResponseCode, Exception ex, HttpServletRequest request) {
		ErrorDto errorDto = new ErrorDto(errorResponseCode, ex.getMessage(), request.getRequestURI());
		ImmutableMap<String, ErrorDto> responseVal = ImmutableMap.of(ERROR, errorDto);
		return ResponseEntity.status(httpStatus).body(responseVal);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		BindingResult result = exception.getBindingResult();
		FieldError error = result.getFieldError();
		String errMsg = errorMsgs.getMessage(error.getDefaultMessage(), null, CommonUtils.getLocale());
		ErrorDto errorDto = new ErrorDto(ErrorResponseCode.BAD_REQUEST, errMsg,
				request.getDescription(false).replace(URI, ""));
		ImmutableMap<String, ErrorDto> responseVal = ImmutableMap.of(ERROR, errorDto);
		return new ResponseEntity<>(responseVal, headers, status);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ImmutableMap<String, ErrorDto>> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpServletRequest request) {
		Throwable throwable = ex.getCause();
		if (throwable instanceof IllegalArgumentException) {
			IllegalArgumentException illegalArgumentException = (IllegalArgumentException) throwable;
			return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorResponseCode.BAD_REQUEST, illegalArgumentException,
					request);
		}
		return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorResponseCode.BAD_REQUEST, ex, request);
	}
}
