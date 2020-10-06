package com.idexcel.cync.los.entity.common.constants;

/**
 * Class for defining constants in error handling for Financial Spreading
 * Application
 * 
 * @author IDEXCEL
 *
 */
public class ErrorMessageConstants {

	private ErrorMessageConstants() {
	}

	public static final String CLIENT_DETAILS_NOT_FOUND = "error.clientdetails.not.found";
	public static final String INVALID_COUNTRY_ID = "error.invalid.countryId";
	public static final String INVALID_STATE_FOR_COUNTRYID = "error.invalidState.for.country.value";
	public static final String ENTITTY_TYPE_NOT_FOUND = "error.entity.type.not.found";
	public static final String INDIVIDUAL_ENTITY_NAME_EXIST = "error.individual.entity.name.exists";
	public static final String COMMERCIAL_ENTITY_NAME_EXIST = "error.commercial.entity.name.exists";
	public static final String COMMERCIAL_ENTITY_NAME_LENGTH = "error.businessName.length";
	public static final String COMMERCIAL_ENTITY_NAME_FORMAT = "error.businessName.format";
	public static final String ENTITY_RELATION_EXIST = "error.entity.relation.exists";
	public static final String ENTITY_RELATION_STATUS_FALSE_EXIST = "error.entity.relation.status.false.exists";
	public static final String ENTITY_ID_NOT_FOUND = "error.entity.id.not.found";
	public static final String ENTITY_NOT_FOUND = "error.entity.not.found";
	public static final String ENTITY_ID_FOUND_SAME = "error.entity.id.same";
	public static final String INVALID_PUT_REQUEST_ID = "error.invalid.put.request.id";
	public static final String ERROR_INVALID_FORMAT_EXCEPTION = "error.invalid.format.exception";
	public static final String INVALID_CLIENT_ID = "error.invalid.client.id";
	public static final String INVALID_CLIENT_ID_CLIENT_NAME = "error.invalid.client.id.client.name";
	public static final String INVALID_SORT_DIRECTION = "error.sortDirection.invalid";
	public static final String INVALID_SORT_BY = "error.sortBy.invalid";
	public static final String INVALID_DATE_OF_BIRTH = "error.invalidDateOfBirth.value";
	public static final String INVALID_BUSINESS_OPEN_DATE = "error.invalidBusinessOpenDate.value";
	public static final String BUSINESS_OPEN_DATE_FORMAT = "error.businessOpenDate.format";
	public static final String INVALID_ENTITY_TYPE_ID = "error.invalid.entity.typeid";
	public static final String INVALID_BUSINESS_TYPE_ID = "error.invalid.businessTypeId";
	public static final String INVALID_CONFIG_TYPE_ID = "error.invalid.configtypeId";
	public static final String INVALID_CONFIG_TYPE_CODE = "error.invalid.configtypeCode";
	public static final String INVALID_ENTITY_ID = "error.invalid.entityId";
	public static final String INVALID_ENTITY_NAME = "error.invalid.entity.name";
	public static final String MISMATCH_CONFIG_ID = "error.mismatch.configId";
	public static final String INVALID_DATE = "error.invalid.date";
	public static final String INVALID_NAICS_CODE_ID = "error.invalid.naics.code.id";
	public static final String INVALID_ENTITTY_TYPE = "error.invalid.entityType";
	public static final String INVALID_RELATION_TYPE_ID = "error.invalid.entityRelationTypeId";
	public static final String INVALID_RELATIONTYPE_ID = "error.invalid.entityRelationId";
	public static final String INVALID_RELATION_IDS = "error.invalid.entityRelationIds";
	public static final String INVALID_RELATION_MAP = "error.invalid.entityRelation.map";
	public static final String INVALID_RELATION_CONFIG_DETAILS = "error.invalid.entityRelation.configDetail";
	public static final String INVALID_RELATION_OWNERSHIP = "error.invalid.entityRelation.ownership";
	public static final String ENTITY_RELATION_NOT_ALLOWED = "error.entityRelation.notAllowed";
	public static final String ENTITY_RELATIONS_OWNERSHIP = "error.entityRelation.ownership";
	public static final String ENTITY_RELATIONS_OWNERSHIP_PERCENT = "error.entityRelation.ownership.percent";
	public static final String LENDER_INFO = "error.lenderInfo.not.present";
	public static final String MULTIPLE_SPOUSE = "error.multiple.spouse.count";

//----------------------------------------------
	public static final String SORT_DIRECTION_ASC = "ASC";// no message
	public static final String GREATER_THAN = ">";// no message
	public static final String LESS_THAN = "<";// no message
	public static final String SORT_DIRECTION_DESC = "DESC";// no message
	public static final String CLIENT_INFO_URL = "client_info_url";// no message
	public static final String PARAMETER_ALREADY_EXISTS = "error.parameter.already.exists";// no message
	public static final String PARAMETER_ID_NOT_NULL = "error.parameter.id.not.null";// no message
	public static final String PARAMETER_NOT_FOUND = "error.parameter.not.found";// no message
	public static final String PARAMETER_USED_IN_FORMULA = "error.parameter.used.in.formula";// no message
	public static final String REMOVE_SPACE_REGEX = "\\s+";// no message
	public static final String SPACE = "";// no message
	public static final String SPECIAL_CHARACTER_NOT_ALLOWED = "error.special.character.not.allowed";// no message
	public static final String IS_DB_MIGRATION_ALLOWED = "app.db-migration.allowed";// no message
	public static final String DB_MIGRATION_DIR_NAME = "app.db-migration.migrationDir";// no message
	public static final String DB_MIGRATION_VERSION = "app.db-migration.migrationVersion";// no message

}
