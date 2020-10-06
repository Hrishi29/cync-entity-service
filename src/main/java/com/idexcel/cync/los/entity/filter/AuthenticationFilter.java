package com.idexcel.cync.los.entity.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.config.AuditorAwareImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * 
 * @author Idexcel
 *
 */
public class AuthenticationFilter extends BasicAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);
	private final Environment env;

	public AuthenticationFilter(AuthenticationManager authManager, Environment env) {
		super(authManager);
		this.env = env;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String authorization = req.getHeader(CoreConstants.AUTHORIZATION);
		if (req.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
			chain.doFilter(req, res);
		} else if (authorization == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "access token is not present");
		} else {
			MDC.put(CoreConstants.AUTHORIZATION, authorization);
			try {
				Claims claims = Jwts.parser().setSigningKey(env.getProperty("SECRET").getBytes())
						.parseClaimsJws(authorization).getBody();
				String lenderName = claims.get("realm").toString();
				if (claims.get(CoreConstants.USER_NAME) == null || claims.get("userId") == null) {
					res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid access token");
				} else {
					MDC.put("API_DOMAIN", env.getProperty("API_DOMAIN"));
					MDC.put(LOSEntityConstants.CLIENT_NAME_KEY, lenderName);
					MDC.put(LOSEntityConstants.CLIENT_ID_KEY, String.valueOf(1001));
					String username = claims.get(CoreConstants.USER_NAME).toString();
					MDC.put(CoreConstants.USER_NAME,username );
					String transactionId = UUID.randomUUID().toString().replaceAll("-", "");
					MDC.put(LOSEntityConstants.TRANSACTION_ID_KEY, transactionId);
					res.addHeader(LOSEntityConstants.TRANSACTION_ID_KEY, transactionId);
					AuditorAwareImpl.setUserId(claims.get("userId").toString());
					AuditorAwareImpl.setUserName(username);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							username, null);
					SecurityContextHolder.getContext().setAuthentication(authentication);
					chain.doFilter(req, res);
				}
			} catch (HttpClientErrorException e) {
				handleAuthorizationServerResponse(e, res);
				LOG.debug("exception from authorization server ", e);
			} catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException
					| IllegalArgumentException e) {
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid access token");
			}
		}
	}

	public void handleAuthorizationServerResponse(HttpClientErrorException e, HttpServletResponse res) {
		LOG.debug("handling error for autherization server error response ");
		try {
			if (e.getStatusCode().name().equalsIgnoreCase(HttpStatus.SERVICE_UNAVAILABLE.name())) {
				res.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "service is unavailable");
				LOG.debug("Error while handling authorization server response ", e);
			}
		} catch (IOException e1) {
			LOG.debug("Error while handling authorization server response ", e1);
		}
	}
}