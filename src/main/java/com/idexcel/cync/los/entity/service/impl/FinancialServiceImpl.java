package com.idexcel.cync.los.entity.service.impl;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITTY_TYPE_NOT_FOUND;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_ENTITY_ID;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.MISMATCH_CONFIG_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.dao.CommercialEntityRepository;
import com.idexcel.cync.los.entity.dao.CommercialNameSearchRepository;
import com.idexcel.cync.los.entity.dao.EntityTypeLookupRepository;
import com.idexcel.cync.los.entity.dao.FinancialEntityListRepository;
import com.idexcel.cync.los.entity.dao.FinancialRepository;
import com.idexcel.cync.los.entity.dao.IndividualEntityRepository;
import com.idexcel.cync.los.entity.dao.IndividualNameSearchRepository;
import com.idexcel.cync.los.entity.dao.LosConfigRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.exception.ResourceNotFoundException;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntityList;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfig;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.service.FinancialService;
import com.idexcel.cync.los.entity.validator.FinancialServiceImplValidator;

/**
 * 
 * @author Idexcel
 *
 */

@Service
public class FinancialServiceImpl implements FinancialService {

	private static final Logger LOG = LoggerFactory.getLogger(FinancialServiceImpl.class);
	private MessageSource errorMsgs;
	private FinancialRepository financialRepository;
	private LosConfigRepository losConfigRepository;
	private EntityTypeLookupRepository entityTypeLookupRepository;
	private IndividualEntityRepository individualEntityRepository;
	private CommercialEntityRepository commercialEntityRepository;
	private FinancialServiceImplValidator financialServiceImplValidator;

	@Autowired
	private IndividualNameSearchRepository individualNameSearchRepository;

	@Autowired
	private CommercialNameSearchRepository commercialNameSearchRepository;

	@Autowired
	FinancialEntityListRepository financialEntityListRepository;

	@Autowired
	public FinancialServiceImpl(MessageSource errorMsgs, FinancialRepository financialRepository,
			LosConfigRepository losConfigRepository, EntityTypeLookupRepository entityTypeLookupRepository,
			IndividualEntityRepository individualEntityRepository,
			CommercialEntityRepository commercialEntityRepository,
			FinancialServiceImplValidator financialServiceImplValidator) {
		this.errorMsgs = errorMsgs;
		this.financialRepository = financialRepository;
		this.losConfigRepository = losConfigRepository;
		this.entityTypeLookupRepository = entityTypeLookupRepository;
		this.individualEntityRepository = individualEntityRepository;
		this.commercialEntityRepository = commercialEntityRepository;
		this.financialServiceImplValidator = financialServiceImplValidator;
	}

	/**
	 * Method to Return List of EntityDetails with SortBy(EntityFields),
	 * sortDirection,Number of Entities per page
	 */
	@Override
	public List<FinancialEntityList> findAllEntity(LosConfigDetails losConfigDetails) {
		if (losConfigDetails.getConfigId() != null) {
			Optional<LosConfigDetails> entityTypeDetails = entityTypeLookupRepository
					.findById(losConfigDetails.getConfigId());
			LOG.debug("Getting Entities with ConfiId(EntityTypeId)");
			return financialEntityListRepository.findByEntityTypeConfig(entityTypeDetails,
					Sort.by(Sort.Order.desc("audit.createdAt")));
		} else {
			LOG.debug("Getting Entities without ConfiId(EntityTypeId)");
			return financialEntityListRepository.findAll(Sort.by(Sort.Order.desc("audit.createdAt")));
		}
	}

	/**
	 * Method for Creating The Entity(Commercial/Individual) and Save to Database
	 * 
	 */
	@Override
	public String saveEntity(FinancialEntity entity, Long entityTypeId) {
		financialServiceImplValidator.isValidClient(entity);
		LosConfig losConfig = losConfigRepository.findById(entityTypeId)
				.orElseThrow(() -> new ResourceNotFoundException(
						errorMsgs.getMessage(ENTITTY_TYPE_NOT_FOUND, null, CommonUtils.getLocale())));
		if ((entity.getEntityTypeConfig() != null) && (entity.getEntityTypeConfig().getConfigId() == null
				|| !entity.getEntityTypeConfig().getConfigId().equals(entityTypeId))) {
			throw new BadRequestException(errorMsgs.getMessage(MISMATCH_CONFIG_ID, new Object[] {
					String.valueOf(entity.getEntityTypeConfig().getConfigId()), String.valueOf(entityTypeId) },
					CommonUtils.getLocale()));
		}
		financialServiceImplValidator.isCountryStateNullOrNot(entity);
		financialServiceImplValidator.isEntitiyTypeConfigIdValid(entity, entityTypeId);
		financialServiceImplValidator.isValidEntity(entity, losConfig);
		financialServiceImplValidator.isValidNaicsCode(entity);
		financialServiceImplValidator.isValidRM(entity);
		entity.setEntityTypeId(entityTypeId);
		LOG.debug("Saving entity..");
		return financialRepository.save(entity).getEntityId();
	}

	/**
	 * Method to Return Entity Based On EntityId if Present
	 */
	@Override
	public FinancialEntity findById(String id) {
		return financialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
				errorMsgs.getMessage(INVALID_ENTITY_ID, new Object[] { id }, CommonUtils.getLocale())));
	}

	/**
	 * Method to Activate Entity
	 */
	@Override
	public void deactivateEntity(String entityId) {
		financialRepository.deactivateEntity(entityId);
	}

	/**
	 * Method to Activate Entity
	 */
	@Override
	public void activateEntity(String entityId) {
		financialRepository.activateEntity(entityId);
	}

	/**
	 * Method to SearchEntityByName returns List of Entity with same or matching
	 * Name(FirstName, LastName, MiddleName & BusinessName)
	 */
	@Override
	public List<FinancialEntity> searchEntityByName(String entityName) {
		List<FinancialEntity> financialEntity = new ArrayList<>();
		List<IndividualFinancialEntity> individualFinancialEntity = null;
		List<CommercialEntity> commercialEntity = commercialEntityRepository
				.findByBusinessNameIgnoreCaseContaining(entityName);
		if (commercialEntity != null) {
			financialEntity.addAll(commercialEntity);
		}
		if (!entityName.isEmpty()) {
			String[] splittedName = entityName.split(" ");
			if (splittedName.length == 1) {
				String splittedFName = splittedName[0];
				individualFinancialEntity = individualEntityRepository
						.findByFirstNameIgnoreCaseContaining(splittedFName);
				if (individualFinancialEntity.isEmpty()) {
					individualFinancialEntity = individualEntityRepository
							.findByLastNameIgnoreCaseContaining(splittedFName);
				}
				financialEntity.addAll(individualFinancialEntity);
				return financialEntity;
			} else if (splittedName.length == 2 && splittedName[1].length() == 1) {
				String splittedFName = splittedName[0];
				String splittedMName = splittedName[1];
				individualFinancialEntity = individualEntityRepository
						.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContaining(splittedFName,
								splittedMName);
				financialEntity.addAll(individualFinancialEntity);
				return financialEntity;
			} else if (splittedName.length == 2 && splittedName[1].length() > 1) {
				String splittedFName = splittedName[0];
				String splittedLName = splittedName[1];
				individualFinancialEntity = individualEntityRepository
						.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(splittedFName,
								splittedLName);
				financialEntity.addAll(individualFinancialEntity);
				return financialEntity;
			} else if (splittedName.length == 3 && splittedName[1].length() == 1) {
				String splittedFName = splittedName[0];
				String splittedMName = splittedName[1];
				String splittedLName = splittedName[2];
				individualFinancialEntity = individualEntityRepository
						.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
								splittedFName, splittedMName, splittedLName);
				financialEntity.addAll(individualFinancialEntity);
				return financialEntity;
			}
		}
		return financialEntity;
	}

	/**
	 * Method to searchDuplicateEntityName returns List of Entity with same or
	 * matching Name(FirstName, LastName, MiddleName & BusinessName)
	 */
	@Override
	public List<FinancialEntity> searchDuplicateEntityName(Long entityTypeId, String businessName, String firstName,
			String middleName, String lastName) {
		List<FinancialEntity> financialEntityList = new ArrayList<>();
		LosConfig losConfig = losConfigRepository.findById(entityTypeId)
				.orElseThrow(() -> new ResourceNotFoundException(
						errorMsgs.getMessage(ENTITTY_TYPE_NOT_FOUND, null, CommonUtils.getLocale())));
		if (losConfig.getConfigCode().equals(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE)) {
			List<IndividualFinancialEntity> individualFinancialEntity = individualNameSearchRepository
					.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(firstName, middleName,
							lastName);
			if (!individualFinancialEntity.isEmpty()) {
				financialEntityList.addAll(individualFinancialEntity);
			}
		} else if (losConfig.getConfigCode().equals(LOSEntityConstants.COMMERCIAL_CONFIG_CODE)) {
			List<CommercialEntity> listcommercialEntity = commercialNameSearchRepository
					.findByBusinessNameIgnoreCase(businessName);
			if (!listcommercialEntity.isEmpty()) {
				financialEntityList.addAll(listcommercialEntity);
			}
		}
		return financialEntityList;
	}

	/**
	 * Method for Updating The Entity(Commercial/Individual) and Save to Database
	 */
	@Override
	public String updateEntity(Long entityTypeId, String entityId, FinancialEntity entity) {
		LOG.debug("updating entity..");
		financialServiceImplValidator.isValidClient(entity);
		FinancialEntity financialEntity = financialRepository.findById(entityId)
				.orElseThrow(() -> new ResourceNotFoundException("Entity id not found: " + entityId));
		entity.setEntityId(financialEntity.getEntityId());
		entity.setEntityTypeId(entityTypeId);
		LosConfig losConfig = losConfigRepository.findById(entityTypeId)
				.orElseThrow(() -> new ResourceNotFoundException(
						errorMsgs.getMessage(ENTITTY_TYPE_NOT_FOUND, null, CommonUtils.getLocale())));
		financialServiceImplValidator.isEntitiyTypeConfigIdValid(entity, entityTypeId);
		financialServiceImplValidator.isValidCreatedAt(entity, entityId);
		financialServiceImplValidator.isCountryStateNullOrNot(entity);
		financialServiceImplValidator.isValidEntity(entity, losConfig);
		financialServiceImplValidator.isValidNaicsCode(entity);
		financialServiceImplValidator.isValidRM(entity);
		return financialRepository.save(entity).getEntityId();
	}

}
