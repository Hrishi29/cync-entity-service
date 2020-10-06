package com.idexcel.cync.los.entity.common.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.exception.AccessDeniedException;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExternalAPICallerTest {

	public static final String ADMIN_RESPONSE = "/test-los-entity/admin-response.json";

	@InjectMocks
	private ExternalAPICaller externalAPICaller;

	@MockBean
	RestTemplate restTemplate;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public <T> void postTest() throws Exception {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class))
				.thenReturn(new ResponseEntity<Object>(ResponseEntity.ok(new ObjectMapper().readValue(Resources
						.toString(Resources.getResource(ExternalAPICallerTest.class, ADMIN_RESPONSE), Charsets.UTF_8),
						Object.class)), HttpStatus.OK));
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
	}

	@Test
	public <T> void postInternalServerErrorTest() {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class)).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}

	@Test
	public <T> void postForbiddenTest() {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class)).thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCodeValue());
	}

	@Test
	public <T> void postBadRequestTest() {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
	}

	@Test(expected = AccessDeniedException.class)
	public <T> void postUnauthorizedTest() {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class)).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
	}
	@Test(expected = NullPointerException.class)
	public <T> void postNullTest() {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY));
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.BAD_GATEWAY.value(), response.getStatusCodeValue());
	}

	@Test
	public <T> void postExceptionTest() {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class)).thenThrow();
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.REQUEST_TIMEOUT.value(), response.getStatusCodeValue());
	}

	@Test
	public <T> void postHttpServerErrorExceptionTest() {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}
	@Test(expected = NullPointerException.class)
	public <T> void postHttpServerErrorExceptionNullTest() {
		T requestBody = null;
		HttpEntity<T> entity = new HttpEntity<>(requestBody, getRequestHeaders(MDC.get(CoreConstants.AUTHORIZATION)));
		Mockito.when(restTemplate.exchange(CoreConstants.HTTP_HEADER + "admin/api/admin", HttpMethod.POST, entity,
				Object.class)).thenThrow(new HttpServerErrorException(HttpStatus.BAD_GATEWAY));
		ResponseEntity<Object> response = externalAPICaller.post(new String("admin/api/admin"), null);
		assertEquals(HttpStatus.BAD_GATEWAY.value(), response.getStatusCodeValue());
	}

	private HttpHeaders getRequestHeaders(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> mediaList = new ArrayList<>();
		mediaList.add(MediaType.APPLICATION_JSON);
		headers.setAccept(mediaList);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(CoreConstants.AUTHORIZATION, authToken);
		return headers;
	}

}
