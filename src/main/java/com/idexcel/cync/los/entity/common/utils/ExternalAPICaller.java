package com.idexcel.cync.los.entity.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.exception.AccessDeniedException;

@Component
public class ExternalAPICaller {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExternalAPICaller.class);
	private RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

	/**
	 * /admin/api/admin/users/queryByEmails
	 * @param <T>
	 * @param urlConstant
	 * @return
	 */
	public <T> ResponseEntity<Object> post(String urlConstant, T requestBody) {
		ResponseEntity<Object> response = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody,
				getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		try {
			response = restTemplate.exchange(CoreConstants.HTTP_HEADER + urlConstant, HttpMethod.POST, entity,
					Object.class);
		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				LOGGER.error("getting 500 from lender specific api {} ", urlConstant);
				return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if (e.getRawStatusCode() == HttpStatus.FORBIDDEN.value()) {
				LOGGER.error("getting 403 from lender specific api {} ", urlConstant);
				return new ResponseEntity<>("Request Forbidden", HttpStatus.FORBIDDEN);
			}
			if (e.getRawStatusCode() == HttpStatus.BAD_REQUEST.value()) {
				LOGGER.error("getting 400  from lender specific api {} ", urlConstant);
				return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
			}
			if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
				LOGGER.error("getting 401 from lender specific api {} ", urlConstant);
				throw new AccessDeniedException("Access is denied");
			}
		} catch (HttpServerErrorException e) {
			if (e.getRawStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				LOGGER.error("getting 500 from lender specific api {} ", urlConstant);
				return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			LOGGER.error("getting 408(Request Timeout)  from lender specific api {} ", urlConstant);
			return new ResponseEntity<>("REQUEST_TIMEOUT", HttpStatus.REQUEST_TIMEOUT);
		}
		return response;
	}
	
	/**
	 * Method to set Headers
	 * 
	 * @param authToken
	 * @return
	 */
	private HttpHeaders getRequestHeaders(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> mediaList = new ArrayList<>();
		mediaList.add(MediaType.APPLICATION_JSON);
		headers.setAccept(mediaList);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(CoreConstants.AUTHORIZATION, authToken);
		return headers;
	}

	@Bean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(httpClient());
		return clientHttpRequestFactory;
	}

	/**
	 * Method to Set Connection Time
	 * 
	 * @return
	 */
	@Bean
	public HttpClient httpClient() {
		final int TIMEOUT_MILLISECOND = 3000;
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(TIMEOUT_MILLISECOND)
				.setConnectTimeout(TIMEOUT_MILLISECOND).setSocketTimeout(TIMEOUT_MILLISECOND).build();
		return HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
	}

}
