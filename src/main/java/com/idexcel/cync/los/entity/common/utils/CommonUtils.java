package com.idexcel.cync.los.entity.common.utils;

import java.util.Locale;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;

public final class CommonUtils {

	private static final Logger LOG = LoggerFactory.getLogger(CommonUtils.class);

	private CommonUtils() {
	}

	/**
	 * Method to get locale
	 * 
	 * @return
	 */
	public static Locale getLocale() {
		return Locale.US;
	}

	public static Long getClientIdFromMDC() {
		Long clientId = Long.parseLong((String) MDC.get(LOSEntityConstants.CLIENT_ID_KEY));
		LOG.debug(" ClientId fetched from MDC == {}", clientId);
		return clientId;
	}

	public static String getClientNameFromMDC() {
		String clientName = (String) MDC.get(LOSEntityConstants.CLIENT_NAME_KEY);
		LOG.info(" ClientName fetched from MDC == {}", clientName);
		return clientName;
	}

	public static String getClientCodeFromMDC() {
		String clientCode = (String) MDC.get(LOSEntityConstants.CLIENT_CODE_KEY);
		LOG.info(" ClientCode fetched from MDC == {}", clientCode);
		return clientCode;
	}

	public static String getEntityTypeSuffixFromMDC() {
		String entityType = (String) MDC.get(LOSEntityConstants.ENTITY_TYPE_KEY);
		LOG.info(" Entity type fetched from MDC == {}", entityType);
		return entityType;
	}
}
