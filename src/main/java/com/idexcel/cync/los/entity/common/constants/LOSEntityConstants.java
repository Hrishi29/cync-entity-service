package com.idexcel.cync.los.entity.common.constants;

public class LOSEntityConstants {

	public LOSEntityConstants() {
	}

	public static final String CLIENT_NAME_KEY = "clientName";
	public static final String CLIENT_CODE_KEY = "clientCode";
	public static final String CLIENT_ID_KEY = "clientId";
	public static final String TRANSACTION_ID_KEY = "transactionId";
	public static final String INDIVIDUAL_CONFIG_CODE = "INDIVIDUAL";
	public static final String COMMERCIAL_CONFIG_CODE = "COMMERCIAL";
	public static final String ENTITY_RELATION_TYPE_CODE = "ENTITY_RELATIONSHIP_TYPE";
	public static final String BUSINESS_TYPE = "BUSINESS_TYPE";
	public static final String ENTITY_TYPE_KEY = "entityType";
	public static final String COUNTRY = "COUNTRY";
	public static final String STATE = "STATE";
	public static final String NAICS_CODE = "NAICS_CODE";
	public static final String INDIVIDUAL_SUFFIX_CODE = "I";
	public static final String COMMERCIAL_SUFFIX_CODE = "C";
	public static final String ROOT_NODE = "rootNode";
	public static final Long AFFILIATED = (long) 3000007;
	public static final Long OWNER = (long) 3000008;
	public static final Long SUBSIDIARY = (long) 3000009;
	public static final Long SPOUSE = (long) 3000010;
	public static final Long OWNED = (long) 3000011;
	public static final String UNIQUE_KEY_LENGTH = "%06d";
	public static final String UNIQUE_KEY_INCREMENT_VALUE = "1";
	public static final String ENTITY_PHONE_REGEX = "^[0-9]*$";
	public static final String ENTITY_ADDRESS_REGEX = "^[a-zA-Z0-9#.,\\'\\s]*$";
	public static final String ENTITY_CITY_REGEX = "^[a-zA-Z0-9#.,\\'\\s]*$";
	public static final String INDIVIDUAL_ENTITY_NAME_REGEX = "^[a-zA-Z]+([a-zA-Z]+)*$";
	public static final String BUSINESS_OPEN_DATE_PATTERN = "MM/dd/yyyy";// MM/dd/yyyy
	public static final String DATE_PATTERN = "(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](18|19|20)\\d\\d";
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String NAME_FORMAT = "^[a-zA-Z0-9!&#']+[!&#'a-zA-Z0-9\\s]+[a-zA-Z0-9]+[a-zA-Z0-9!&#'\\s]*$";
	public static final String ZIPCODE_FORMAT = "^\\d{5}(?:[-]\\d{4})?$";
	public static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

	public static final String APP_NAME ="appName";
	public static final String CLIENT ="client";
	public static final String SERVICE_NAME ="serviceName";
	public static final String RECORD_NAME ="recordName";
	public static final String OPERATION ="operation";
	public static final String ID ="Id";
	public static final String USER_ACTION ="userAction";
	public static final String STATUS ="status";
	public static final String REQUESTIDENTIFIER ="requestIdentifier";
	public static final String USER_EMAIL ="userEmail";
	public static final String TIMESTAMP ="timestamp";
	public static final String CYNC_LOS ="CYNC-LOS";
	public static final String APPLICATION_NAME ="Entity";
	public static final String TRANSACTION_ID ="x-amzn-requestId";
	
	
	
}
