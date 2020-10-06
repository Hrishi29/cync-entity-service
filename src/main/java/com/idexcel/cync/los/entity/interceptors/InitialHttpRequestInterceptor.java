package com.idexcel.cync.los.entity.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;

@Component
public class InitialHttpRequestInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(InitialHttpRequestInterceptor.class);
	private static final String LOG_PRE_HANDLER = "PRE-HANDLER  :: ";
	private static final String LOG_POST_HANDLER = "POST-HANDLER :: ";
	private static final String LOG_REQ_COMPLETION_HANDLER = "AFTER COMPLETION :: ";
	private static final String START_TIME_KEY = "startTime";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long startTime = System.currentTimeMillis();
		LOG.debug(LOG_PRE_HANDLER + "Request URL == {}", request.getRequestURL());
		LOG.debug(LOG_PRE_HANDLER + "Request Start Time == {}", startTime);
		request.setAttribute(START_TIME_KEY, startTime);
		String methodType = request.getMethod();
		if (HttpMethod.OPTIONS.name().equalsIgnoreCase(methodType)) {
			return true;
		}
		String lenderId = MDC.get(LOSEntityConstants.CLIENT_NAME_KEY);
		LOG.debug(LOG_PRE_HANDLER + "lenderId == {} ", lenderId);
		if (lenderId == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access is denied");
			return false;
		} else {
			request.setAttribute(CoreConstants.CURRENT_TENANT, lenderId);
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		LOG.debug(LOG_POST_HANDLER + " Authentication Information Removed");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long startTime = (Long) request.getAttribute(START_TIME_KEY);
		long endTime = System.currentTimeMillis();
		LOG.debug(LOG_REQ_COMPLETION_HANDLER + "Request End Time == {}", endTime);
		LOG.debug(LOG_REQ_COMPLETION_HANDLER + "Total Time taken to process this request ==  {} ",
				(endTime - startTime));
	}
}
