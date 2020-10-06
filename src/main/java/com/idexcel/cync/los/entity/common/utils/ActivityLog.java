package com.idexcel.cync.los.entity.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.gson.Gson;
import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.dto.IndividualEntityDto;

public class ActivityLog {

	private static final Logger LOG = LoggerFactory.getLogger(ActivityLog.class);

	public static String getActivityLog(String entityName, Operation operation, String entityId, String userAction,
			Status status, String requestIdentifier, LocalDateTime timestamp) {
		Map<String, String> json = new LinkedHashMap<String, String>();
		json.put(LOSEntityConstants.APP_NAME, LOSEntityConstants.CYNC_LOS);
		json.put(LOSEntityConstants.CLIENT, MDC.get(LOSEntityConstants.CLIENT_NAME_KEY));
		json.put(LOSEntityConstants.SERVICE_NAME, LOSEntityConstants.APPLICATION_NAME);
		json.put(LOSEntityConstants.RECORD_NAME, entityName);
		json.put(LOSEntityConstants.OPERATION, operation.toString());
		json.put(LOSEntityConstants.ID, entityId);
		json.put(LOSEntityConstants.USER_ACTION, userAction);
		json.put(LOSEntityConstants.STATUS, status.toString());
		json.put(LOSEntityConstants.REQUESTIDENTIFIER, requestIdentifier);
		json.put(LOSEntityConstants.USER_EMAIL, MDC.get(CoreConstants.USER_NAME));
		json.put(LOSEntityConstants.TIMESTAMP, timestamp.toString());
		String jsonData = null;
		try {
			jsonData = new Gson().toJson(json, Object.class);
		} catch (Exception e) {
			LOG.info("Error while printing the activity log {}" + e.getMessage());
		}
		return jsonData;
	}

	/**
	 * Method to convert local time into UTC
	 * 
	 * @return
	 */
	public static LocalDateTime localDateTimeInUTC() {
		return LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
				.toLocalDateTime();
	}

	/**
	 * Method to return IndividualEntity Name
	 * 
	 * @param individualFinancialEntityDto
	 * @return
	 */
	public static String entityName(IndividualEntityDto individualFinancialEntityDto) {
		String nameString = null;
		if (individualFinancialEntityDto.getMiddleName() == null) {
			nameString = individualFinancialEntityDto.getFirstName() + " " + individualFinancialEntityDto.getLastName();
		} else {
			nameString = individualFinancialEntityDto.getFirstName() + " "
					+ individualFinancialEntityDto.getMiddleName() + " " + individualFinancialEntityDto.getLastName();
		}
		return nameString;
	}

}
