package com.idexcel.cync.los.entity.common.constants;

import org.slf4j.MDC;

public class CoreConstants {

	private CoreConstants() {
	}

	public static final String API_VERSION = "/v1";
	public static final String API_BASE_PATH = API_VERSION;

	public static final String HTTP_HEADER = "https://";
	public static final String AUTHORIZATION = "Authorization";

	// Environment Variable
	public static final String DEV_URL = MDC.get("API_DOMAIN");
	public static final String ADMIN_GET_USER_URL = DEV_URL + "/admin/api/admin/users/";
	public static final String ADMIN_POST_USER_URL = DEV_URL + "/admin/api/admin/users/queryByEmails";
	
	public static final String CURRENT_TENANT = "CURRENT_TENANT_IDENTIFIER";
	public static final String LOAS_ENTITY_BASE_PACKAGE = "com.idexcel.cync.los.entity";
	public static final String USER_NAME = "username";

}
