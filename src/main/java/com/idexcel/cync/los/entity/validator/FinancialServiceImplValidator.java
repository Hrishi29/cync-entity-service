package com.idexcel.cync.los.entity.validator;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_BUSINESS_OPEN_DATE;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_DATE_OF_BIRTH;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_ENTITY_TYPE_ID;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_NAICS_CODE_ID;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.MISMATCH_CONFIG_ID;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.ActivityLog;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.common.utils.ExternalAPICaller;
import com.idexcel.cync.los.entity.common.utils.Operation;
import com.idexcel.cync.los.entity.common.utils.Status;
import com.idexcel.cync.los.entity.dao.ConfigLookupRepository;
import com.idexcel.cync.los.entity.dao.FinancialRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfig;
import com.idexcel.cync.los.entity.model.LosConfigDetails;

@Service
public class FinancialServiceImplValidator {

	private static final Logger LOG = LoggerFactory.getLogger(FinancialServiceImplValidator.class);
	private final MessageSource errorMsgs;
	private FinancialRepository financialRepository;
	private ConfigLookupRepository configLookupRepository;
	private ClientValidator clientValidator;
	private FSCountryStateValidator fSCountryStateValidator;

	@Autowired
	public FinancialServiceImplValidator(FinancialRepository financialRepository,
			ConfigLookupRepository configLookupRepository, ClientValidator clientValidator,
			FSCountryStateValidator fSCountryStateValidator, MessageSource errorMsgs) {
		this.errorMsgs = errorMsgs;
		this.financialRepository = financialRepository;
		this.configLookupRepository = configLookupRepository;
		this.clientValidator = clientValidator;
		this.fSCountryStateValidator = fSCountryStateValidator;
	}

	/**
	 * Method To validate Is Entity(Commercial/Individual)
	 * 
	 * @param entity
	 * @param losConfig
	 */
	public void isValidEntity(FinancialEntity entity, LosConfig losConfig) {
		Date date = new Date();
		if (entity instanceof CommercialEntity
				&& losConfig.getConfigCode().equals(LOSEntityConstants.COMMERCIAL_CONFIG_CODE)) {
			isBusinessTypePresent(entity);
			if ((((CommercialEntity) entity).getBusinessOpenDate() != null)
					&& ((CommercialEntity) entity).getBusinessOpenDate().compareTo(date) >= 0) {
				LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
						"Entered BusinessOpenDate greater than today's date", Status.ERROR, null,
						ActivityLog.localDateTimeInUTC()));
				throw new BadRequestException(
						errorMsgs.getMessage(INVALID_BUSINESS_OPEN_DATE, null, CommonUtils.getLocale()));
			}
		} else if (entity instanceof IndividualFinancialEntity
				&& losConfig.getConfigCode().equals(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE)) {
			if ((((IndividualFinancialEntity) entity).getDateOfBirth() != null)
					&& ((IndividualFinancialEntity) entity).getDateOfBirth().compareTo(date) >= 0) {
				LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
						"Entered DateOfBirth greater than today's date", Status.ERROR, null,
						ActivityLog.localDateTimeInUTC()));
				throw new BadRequestException(
						errorMsgs.getMessage(INVALID_DATE_OF_BIRTH, null, CommonUtils.getLocale()));
			}
		} else {
			LOG.debug("invalid EntityTypeId in isValidEntity method ");
			LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null, "Entered Invalid EntityTypeId",
					Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
			throw new BadRequestException(errorMsgs.getMessage(INVALID_ENTITY_TYPE_ID, null, CommonUtils.getLocale()));
		}
	}

	/**
	 * Method To validate the Relationship Manager Id Exist/ valid exchanging the
	 * data form AdminMicroservice
	 * 
	 * @param entity
	 */
	public void isValidRM(FinancialEntity entity) {
		ExternalAPICaller externalAPICaller = new ExternalAPICaller();
		if (entity.getRelationshipManagerEmail() == null || entity.getRelationshipManagerEmail().isEmpty()) {
			entity.setRelationshipManagerEmail(null);
		} else if (!entity.getRelationshipManagerEmail().isEmpty() && entity.getRelationshipManagerEmail() != null) {
			ResponseEntity<Object> responseEntity = externalAPICaller.post(CoreConstants.ADMIN_POST_USER_URL,
					Arrays.asList(entity.getRelationshipManagerEmail()));
			if ((responseEntity.getBody().equals(Arrays.asList()))
					|| responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value()) {
				LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
						"Entered Invalid RelationshipManagerEmail :" + entity.getRelationshipManagerEmail(),
						Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
				throw new BadRequestException(
						"Invalid RelationshipManagerEmail : " + entity.getRelationshipManagerEmail());
			} else if (responseEntity.getStatusCodeValue() > HttpStatus.OK.value()) {
				LOG.info("ErrorCode: {} ",responseEntity.getStatusCodeValue());
				throw new BadRequestException("Error while calling RelationshipManager from External API");
			} else if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				isValidRoleForRelationshipManager(responseEntity);
			}
		}
	}

	/**
	 * Method To validate the Relationship Manager Role allow only Loan Officer,
	 * Credit Analyst, Portfolio Manager
	 * 
	 * @param responseEntity
	 */
	public void isValidRoleForRelationshipManager(ResponseEntity<Object> responseEntity) {
		JSONArray array = new JSONObject(responseEntity).getJSONArray("body");
		for (int i = 0; i < array.length(); i++) {
			if (array.getJSONObject(i).getBoolean("isActive") == Boolean.TRUE) {
				JSONObject role = (JSONObject) array.getJSONObject(i).get("role");
				if ((role.get("isBase") == Boolean.TRUE
						&& !(Arrays.asList("Loan Officer", "Credit Analyst", "Portfolio Manager")
								.contains(role.get("name")))
						|| (role.get("isBase") == Boolean.FALSE
								&& !(Arrays.asList("Loan Officer", "Credit Analyst", "Portfolio Manager")
										.contains(role.get("baseRole")))))) {
					LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
							"Entered invalid role of RelationshipManagerEmail", Status.ERROR, null,
							ActivityLog.localDateTimeInUTC()));
					throw new BadRequestException(
							"RelationshipManager's emailId, having Role:" + role.get("name") + " is Not Permitted ");
				}
			} else {
				throw new BadRequestException("Please check the status of RelationshipManager ");
			}
		}
	}

	/**
	 * Method to Validate NaicsCode Present in SeedData
	 * 
	 * @param entity
	 */
	public void isValidNaicsCode(FinancialEntity entity) {
		if (entity instanceof CommercialEntity) {
			if (((CommercialEntity) entity).getNaics() == null
					|| ((CommercialEntity) entity).getNaics().getConfigId() == null) {
				((CommercialEntity) entity).setNaics(null);
			} else {
				Long naicsCodeId = ((CommercialEntity) entity).getNaics().getConfigId();
				LosConfigDetails naicsCode = configLookupRepository.findById(naicsCodeId)
						.orElseThrow(() -> new BadRequestException(errorMsgs.getMessage(INVALID_NAICS_CODE_ID,
								new Object[] { String.valueOf(naicsCodeId) }, CommonUtils.getLocale())));
				if (!naicsCode.getConfigtypeCode().equals(LOSEntityConstants.NAICS_CODE)) {
					LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
							"Entered invalid NAICS Code", Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
					throw new BadRequestException("Please enter valid NAICS Code");
				}
			}
		}
	}

	/**
	 * ` Method to Validate BusinessTypeId
	 * 
	 * @param entity
	 */
	public void isBusinessTypePresent(FinancialEntity entity) {
		CommercialEntity commercialEntity = (CommercialEntity) entity;
		if ((commercialEntity.getBusinessType() == null) || commercialEntity.getBusinessType().getConfigId() == null) {
			commercialEntity.setBusinessType(null);
		} else {
			Long businesstypeId = commercialEntity.getBusinessType().getConfigId();
			if (businesstypeId != 0) {
				Optional<LosConfigDetails> losConfigBusinessTypeDetails = configLookupRepository
						.findById(businesstypeId);
				if ((!losConfigBusinessTypeDetails.isPresent()) || (!losConfigBusinessTypeDetails.get()
						.getConfigtypeCode().equals(LOSEntityConstants.BUSINESS_TYPE))) {
					LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
							"Entered invalid BusinessTypeId", Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
					throw new BadRequestException("Invalid BusinessTypeId:" + businesstypeId);
				}
			} else {
				throw new BadRequestException("Invalid BusinessTypeId:" + businesstypeId);
			}
		}
	}

	/**
	 * Method To validate CreatedAt
	 * 
	 * @param entityId
	 */
	public void isValidCreatedAt(FinancialEntity entity, String entityId) {
		Optional<FinancialEntity> entityDetails = financialRepository.findById(entityId);
		if (entityDetails.isPresent() && (entity.getAudit() != null
				&& !(entity.getAudit().getCreatedAt()).equals(entityDetails.get().getAudit().getCreatedAt()))) {
			throw new BadRequestException("createdAt Cannot Be Modified/Changed");
		}
	}

	/**
	 * Method to Validate EntitiyTypeConfigIdValid
	 * 
	 * @param entity
	 * @param entityTypeId
	 */
	public void isEntitiyTypeConfigIdValid(FinancialEntity entity, Long entityTypeId) {
		if ((entity.getEntityTypeConfig() != null) && (entity.getEntityTypeConfig().getConfigId() == null
				|| !entity.getEntityTypeConfig().getConfigId().equals(entityTypeId))) {
			throw new BadRequestException(errorMsgs.getMessage(MISMATCH_CONFIG_ID, new Object[] {
					String.valueOf(entity.getEntityTypeConfig().getConfigId()), String.valueOf(entityTypeId) },
					CommonUtils.getLocale()));
		}
	}

	public void isValidClient(FinancialEntity entity) {
		clientValidator.isValidClient(entity);
	}

	public void isCountryStateNullOrNot(FinancialEntity entity) {
		fSCountryStateValidator.isCountryStateNullOrNot(entity);
	}
}
