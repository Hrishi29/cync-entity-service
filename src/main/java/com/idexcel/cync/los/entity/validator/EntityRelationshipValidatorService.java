package com.idexcel.cync.los.entity.validator;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_ID_FOUND_SAME;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_ID_NOT_FOUND;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_RELATIONS_OWNERSHIP;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_RELATIONS_OWNERSHIP_PERCENT;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_RELATION_EXIST;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_RELATION_NOT_ALLOWED;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_RELATION_STATUS_FALSE_EXIST;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_RELATION_CONFIG_DETAILS;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_RELATION_IDS;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_RELATION_OWNERSHIP;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_RELATION_TYPE_ID;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.MULTIPLE_SPOUSE;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.dao.ChildEntityNodeRepository;
import com.idexcel.cync.los.entity.dao.ConfigLookupRepository;
import com.idexcel.cync.los.entity.dao.EntityRelationshipTypeRepository;
import com.idexcel.cync.los.entity.dao.FinancialRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.model.ChildEntityNode;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.service.impl.EntityRelationshipTreeServiceImpl;

@Service
public class EntityRelationshipValidatorService {

	private static final Logger LOG = LoggerFactory.getLogger(EntityRelationshipValidatorService.class);

	private final FinancialRepository financialRepository;
	private final ConfigLookupRepository configLookupRepository;
	private final EntityRelationshipTypeRepository entityRelationshipTypeRepository;
	private final ChildEntityNodeRepository childEntityNodeRepository;
	private final MessageSource errorMsgs;
	private final EntityRelationshipTreeServiceImpl entityRelationshipTreeServiceImpl;

	@Autowired
	public EntityRelationshipValidatorService(FinancialRepository financialRepository,
			EntityRelationshipTypeRepository entityRelationshipTypeRepository,
			ChildEntityNodeRepository childEntityNodeRepository, ConfigLookupRepository configLookupRepository,
			EntityRelationshipTreeServiceImpl entityRelationshipTreeServiceImpl, MessageSource errorMsgs) {
		this.financialRepository = financialRepository;
		this.configLookupRepository = configLookupRepository;
		this.entityRelationshipTypeRepository = entityRelationshipTypeRepository;
		this.childEntityNodeRepository = childEntityNodeRepository;
		this.errorMsgs = errorMsgs;
		this.entityRelationshipTreeServiceImpl = entityRelationshipTreeServiceImpl;
	}

	/**
	 * Method to ValidEntityRelation
	 * 
	 * @param entityRelationshipType
	 * @param entityId1
	 */
	public void isValidEntityRelation(EntityRelationshipType entityRelationshipType, String entityId1) {
		FinancialEntity parentEntityId = financialRepository.findById(entityId1)
				.orElseThrow(() -> new BadRequestException(errorMsgs.getMessage(ENTITY_ID_NOT_FOUND,
						new Object[] { entityId1 }, CommonUtils.getLocale())));
		FinancialEntity childEntityId = financialRepository.findById(entityRelationshipType.getEntityId2())
				.orElseThrow(() -> new BadRequestException(errorMsgs.getMessage(ENTITY_ID_NOT_FOUND,
						new Object[] { entityRelationshipType.getEntityId2() }, CommonUtils.getLocale())));
		// condition to validate parentId childId cannot be same
		if (parentEntityId.getEntityId().equals(childEntityId.getEntityId())) {
			throw new BadRequestException(errorMsgs.getMessage(INVALID_RELATION_IDS,
					new Object[] { entityRelationshipType.getEntityId2(), entityId1 }, CommonUtils.getLocale()));
		}
		// condition to validate EntityRelationConfigDetail
		if (entityRelationshipType.getEntityRelationConfigDetail() == null) {
			throw new BadRequestException(
					errorMsgs.getMessage(INVALID_RELATION_CONFIG_DETAILS, null, CommonUtils.getLocale()));
		} else {
			if (getRelationId(entityRelationshipType) == null || getRelationId(entityRelationshipType) == 0) {
				throw new BadRequestException(
						errorMsgs.getMessage(INVALID_RELATION_CONFIG_DETAILS, null, CommonUtils.getLocale()));
			}
		}
		Optional<LosConfigDetails> entityRelationConfigDetails = configLookupRepository
				.findById(getRelationId(entityRelationshipType));
		if ((entityRelationConfigDetails.isPresent() && !entityRelationConfigDetails.get().getConfigtypeCode()
				.equals(LOSEntityConstants.ENTITY_RELATION_TYPE_CODE)) || !entityRelationConfigDetails.isPresent()) {
			throw new BadRequestException(
					errorMsgs.getMessage(INVALID_RELATION_TYPE_ID, null, CommonUtils.getLocale()));
		}
		if (entityRelationshipType.getOwnership() == null) {
			throw new BadRequestException(
					errorMsgs.getMessage(INVALID_RELATION_OWNERSHIP, null, CommonUtils.getLocale()));
		}
		if (entityId1.equals(entityRelationshipType.getEntityId2())) {
			throw new BadRequestException(errorMsgs.getMessage(ENTITY_ID_FOUND_SAME, null, CommonUtils.getLocale()));
		}

		List<ChildEntityNode> childEntityNode = childEntityNodeRepository.findEntityTreeData(entityId1);
		if (!childEntityNode.isEmpty()) {
			entityRelationshipTreeServiceImpl.validateTree(childEntityNode, entityId1,
					entityRelationshipType.getEntityId2());
		}
		validateEntityRelationCombination(entityId1, entityRelationshipType);
		validateEntityReverseRelationCombination(entityId1, entityRelationshipType);
	}

	public void validateEntityReverseRelationCombination(String entityId1,
			EntityRelationshipType entityRelationshipType) {
		EntityRelationshipType existingCombinationE1E2 = entityRelationshipTypeRepository
				.findByEntityId1AndEntityId2AndDeleted(entityId1, entityRelationshipType.getEntityId2(), false);
		EntityRelationshipType existingCombinationE2E1 = entityRelationshipTypeRepository
				.findByEntityId2AndEntityId1AndDeleted(entityId1, entityRelationshipType.getEntityId2(), false);
		if ((existingCombinationE1E2 != null && (existingCombinationE1E2.isStatus() == Boolean.TRUE))
				|| (existingCombinationE2E1 != null) && (existingCombinationE2E1.isStatus() == Boolean.TRUE)) {
			throw new BadRequestException(errorMsgs.getMessage(ENTITY_RELATION_EXIST,
					new Object[] { entityId1, entityRelationshipType.getEntityId2() }, CommonUtils.getLocale()));
		} else if ((existingCombinationE1E2 != null && (existingCombinationE1E2.isStatus() == Boolean.FALSE))
				|| (existingCombinationE2E1 != null) && (existingCombinationE2E1.isStatus() == Boolean.FALSE)) {
			throw new BadRequestException(errorMsgs.getMessage(ENTITY_RELATION_STATUS_FALSE_EXIST,
					new Object[] { entityId1, entityRelationshipType.getEntityId2() }, CommonUtils.getLocale()));
		}
	}

	/**
	 * Method to validate EntityRelationCombinations
	 * 
	 * @param entityId1
	 * @param entityRelationshipType
	 */
	public void validateEntityRelationCombination(String entityId1, EntityRelationshipType entityRelationshipType) {
		String entityId2 = entityRelationshipType.getEntityId2();
		Long relationId = getRelationId(entityRelationshipType);
		if ((entityId1.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& entityId2.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& relationId.equals(LOSEntityConstants.AFFILIATED) && entityId1.equals(entityId2))
				|| (entityId1.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
						&& entityId2.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
						&& relationId.equals(LOSEntityConstants.SUBSIDIARY)
						|| relationId.equals(LOSEntityConstants.OWNER))
				|| (entityId1.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
						&& entityId2.endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
						&& relationId.equals(LOSEntityConstants.OWNER))
				|| (entityId1.endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
						&& entityId2.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
						&& relationId.equals(LOSEntityConstants.OWNED))) {
			validateHundredPercentOwnership(entityRelationshipType.getOwnership());
			LOG.debug("OWNER/OWNED , OWNER/SUBSIDIARY Vice versa - entity realtion");
		} else if (entityId1.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& entityId2.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& relationId.equals(LOSEntityConstants.AFFILIATED)) {
			validateEntityRelationCombinationsOwnership(entityRelationshipType.getOwnership());
			LOG.debug("AFFILIATED/SUBSIDIARY entity realtion");
		} else if (entityId1.endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
				&& entityRelationshipType.getEntityId2().endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
				&& getRelationId(entityRelationshipType).equals(LOSEntityConstants.SPOUSE)) {
			validateEntityRelationCombinationsOwnership(entityRelationshipType.getOwnership());
			LOG.debug("SPOUSE entity realtion");
		} else {
			throw new BadRequestException(
					errorMsgs.getMessage(ENTITY_RELATION_NOT_ALLOWED, null, CommonUtils.getLocale()));
		}
	}

	/**
	 * Method to return Relation Id
	 * 
	 * @param entityRelationshipType
	 * @return relationId
	 */
	private Long getRelationId(EntityRelationshipType entityRelationshipType) {
		return entityRelationshipType.getEntityRelationConfigDetail().getConfigId();
	}

	/**
	 * Method to validate EntityRelationCombinations Ownership cannot be greater
	 * than 100 or lesser than 0, and not equal to null
	 * 
	 * @param ownership
	 */
	private void validateHundredPercentOwnership(Double ownership) {
		if (ownership == null || ownership > 100 || ownership < 0) {
			throw new BadRequestException(
					errorMsgs.getMessage(ENTITY_RELATIONS_OWNERSHIP_PERCENT, null, CommonUtils.getLocale()));
		}
	}

	/**
	 * Method to validate EntityRelationCombinations Ownership
	 * 
	 * @param ownership
	 */
	public void validateEntityRelationCombinationsOwnership(Double ownership) {
		if (ownership == null || ownership > 0 || ownership < 0) {
			throw new BadRequestException(
					errorMsgs.getMessage(ENTITY_RELATIONS_OWNERSHIP, null, CommonUtils.getLocale()));
		}
	}

	/**
	 * Method to validate Spouse(Only One Spouse is allowed) if multiple spouse
	 * throws error
	 * 
	 * @param entityRelationshipType
	 * @param entityId1
	 */
	public void isValidSpouseCount(EntityRelationshipType entityRelationshipType, String entityId1) {
		if (entityId1.endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
				&& getRelationId(entityRelationshipType).equals(LOSEntityConstants.SPOUSE)) {
			int entityId1Column = entityRelationshipTypeRepository.findByEntityId1SpouseCount(entityId1,
					LOSEntityConstants.SPOUSE);
			int entityId1Column2 = entityRelationshipTypeRepository
					.findByEntityId1SpouseCount(entityRelationshipType.getEntityId2(), LOSEntityConstants.SPOUSE);
			int entityId2Column = entityRelationshipTypeRepository
					.findByEntityId2SpouseCount(entityRelationshipType.getEntityId2(), LOSEntityConstants.SPOUSE);
			int entityId2Column2 = entityRelationshipTypeRepository.findByEntityId2SpouseCount(entityId1,
					LOSEntityConstants.SPOUSE);
			if (entityId1Column >= 1 || entityId2Column >= 1 || entityId1Column2 >= 1 || entityId2Column2 >= 1) {
				throw new BadRequestException(errorMsgs.getMessage(MULTIPLE_SPOUSE, null, CommonUtils.getLocale()));
			}
		}
	}
}