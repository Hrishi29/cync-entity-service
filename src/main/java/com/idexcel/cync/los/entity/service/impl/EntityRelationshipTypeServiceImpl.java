package com.idexcel.cync.los.entity.service.impl;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_RELATIONTYPE_ID;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_RELATION_TYPE_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.ActivityLog;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.common.utils.Operation;
import com.idexcel.cync.los.entity.common.utils.Status;
import com.idexcel.cync.los.entity.dao.ChildEntityNodeRepository;
import com.idexcel.cync.los.entity.dao.ConfigLookupRepository;
import com.idexcel.cync.los.entity.dao.EntityNodeRepository;
import com.idexcel.cync.los.entity.dao.EntityRelationshipTypeRepository;
import com.idexcel.cync.los.entity.dao.IndividualEntityRepository;
import com.idexcel.cync.los.entity.dao.ParentEntityNodeRepository;
import com.idexcel.cync.los.entity.dto.EntityNodeSpouseDto;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.exception.ResourceNotFoundException;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.EntityNode;
import com.idexcel.cync.los.entity.model.EntityNodeSpouse;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.ParentEntityNode;
import com.idexcel.cync.los.entity.service.EntityRelationshipTypeService;
import com.idexcel.cync.los.entity.validator.ClientValidator;
import com.idexcel.cync.los.entity.validator.EntityRelationshipValidatorService;

@Service
public class EntityRelationshipTypeServiceImpl implements EntityRelationshipTypeService {

	private static final Logger LOG = LoggerFactory.getLogger(EntityRelationshipTypeServiceImpl.class);

	private final EntityNodeRepository entityNodeRepository;
	private final ConfigLookupRepository configLookupRepository;
	private final EntityRelationshipTypeRepository entityRelationshipTypeRepository;
	private final EntityRelationshipValidatorService entityRelationshipValidator;
	private final ParentEntityNodeRepository parentEntityNodeRepository;
	private final ChildEntityNodeRepository childEntityNodeRepository;
	private final EntityRelationshipTreeServiceImpl entityRelationshipTreeServiceImpl;

	@Autowired
	private MessageSource errorMsgs;

	@Autowired
	private ClientValidator clientValidator;

	@Autowired
	private IndividualEntityRepository individualEntityRepository;

	@Autowired
	public EntityRelationshipTypeServiceImpl(EntityNodeRepository entityNodeRepository,
			EntityRelationshipTypeRepository entityRelationshipTypeRepository,
			ConfigLookupRepository configLookupRepository,
			EntityRelationshipValidatorService entityRelationshipValidator,
			ParentEntityNodeRepository parentEntityNodeRepository, ChildEntityNodeRepository childEntityNodeRepository,
			EntityRelationshipTreeServiceImpl entityRelationshipTreeServiceImpl) {
		this.entityNodeRepository = entityNodeRepository;
		this.configLookupRepository = configLookupRepository;
		this.entityRelationshipTypeRepository = entityRelationshipTypeRepository;
		this.entityRelationshipValidator = entityRelationshipValidator;
		this.parentEntityNodeRepository = parentEntityNodeRepository;
		this.childEntityNodeRepository = childEntityNodeRepository;
		this.entityRelationshipTreeServiceImpl = entityRelationshipTreeServiceImpl;
	}

	public Long saveEntityRelation(EntityRelationshipType entityRelationshipType, String entityId1) {
		clientValidator.isValidClient(entityRelationshipType);
		entityRelationshipValidator.isValidEntityRelation(entityRelationshipType, entityId1);
		entityRelationshipValidator.isValidSpouseCount(entityRelationshipType, entityId1);
		entityRelationshipType.setEntityId1(entityId1);
		return entityRelationshipTypeRepository.save(entityRelationshipType).getEntityRelationshiptypeId();
	}

	@Override
	public EntityNode findEntityRelationTreeByEntityId(String entityId1) {
		List<EntityNode> entityNodes = entityNodeRepository.findEntityTreeData(entityId1);
		return entityRelationshipTreeServiceImpl.createTree(entityNodes, entityId1);
	}

	public int findParentEntityIsNotDeletedCount(String entityId) {
		return childEntityNodeRepository.countByParentIdAndDeleted(entityId, false);
	}

	/**
	 * Method to return the findEntityRelationList By given EntityId
	 */
	@Override
	public List<EntityRelationshipType> findEntityRelationListByEntityId(String entityId) {
		List<EntityRelationshipType> relationList = new ArrayList<>();
		LosConfigDetails owner = configLookupRepository.findByConfigId(LOSEntityConstants.OWNER);
		LosConfigDetails owned = configLookupRepository.findByConfigId(LOSEntityConstants.OWNED);
		LosConfigDetails affilated = configLookupRepository.findByConfigId(LOSEntityConstants.AFFILIATED);
		LosConfigDetails subsidary = configLookupRepository.findByConfigId(LOSEntityConstants.SUBSIDIARY);
		int parentCount = findParentEntityIsNotDeletedCount(entityId);
		if (parentCount == 0) {
			List<String> commercialEntityId2List = new ArrayList<String>();
			List<String> cToCRelation = new ArrayList<String>();
			List<String> iToCRelation = new ArrayList<String>();
			CommercialEntity parentEntity = new CommercialEntity();
			List<EntityRelationshipType> parents = entityRelationshipTypeRepository
					.findByEntityId1AndDeletedAndStatus(Arrays.asList(entityId));
			if (parents.isEmpty()) {
				return entityRelationshipTypeRepository.findByEntityId2AndDeleted(Arrays.asList(entityId));
			}
			parentNotPresent(entityId, relationList, owner, affilated, subsidary, commercialEntityId2List, cToCRelation,
					iToCRelation, parentEntity, parents);
			relationList.addAll(parents);
		} else {
			List<EntityRelationshipType> children = parentPresent(entityId, relationList, owner, owned, affilated,
					subsidary);
			relationList.addAll(children);
		}
		return relationList;
	}

	/**
	 * Method to find the Data if parentPresent for Given entityId
	 * 
	 * @param entityId
	 * @param relationList
	 * @param owner
	 * @param owned
	 * @param affilated
	 * @param subsidary
	 * @return
	 */
	private List<EntityRelationshipType> parentPresent(String entityId, List<EntityRelationshipType> relationList,
			LosConfigDetails owner, LosConfigDetails owned, LosConfigDetails affilated, LosConfigDetails subsidary) {
		List<EntityRelationshipType> affialiateDataList = new ArrayList<>();
		List<EntityRelationshipType> child = new ArrayList<>();
		List<String> individualEntityId2List = new ArrayList<String>();
		List<String> commercialEntityId2List = new ArrayList<String>();
		List<String> childrenEntityId2List = new ArrayList<String>();
		List<EntityRelationshipType> children = entityRelationshipTypeRepository
				.findByEntityId1AndDeletedAndStatus(Arrays.asList(entityId));
		List<EntityRelationshipType> parents = entityRelationshipTypeRepository
				.findByEntityId2AndDeleted(Arrays.asList(entityId));
		parents.stream().forEach(getIndividualEntityId2 -> {
			if ((getRelationTypeId(getIndividualEntityId2).equals(LOSEntityConstants.OWNER)
					|| getRelationTypeId(getIndividualEntityId2).equals(LOSEntityConstants.OWNED))
					&& getIndividualEntityId2.getEntityId2().endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)) {
				individualEntityId2List.add(getIndividualEntityId2.getEntityId2());
			} else if (getIndividualEntityId2.getEntityId2().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
					&& (getRelationTypeId(getIndividualEntityId2).equals(LOSEntityConstants.SUBSIDIARY)
							|| getRelationTypeId(getIndividualEntityId2).equals(LOSEntityConstants.AFFILIATED)
							|| getRelationTypeId(getIndividualEntityId2).equals(LOSEntityConstants.OWNER))) {
				commercialEntityId2List.add(getIndividualEntityId2.getEntityId2());
			}
		});
		children.forEach(getEntityId2 -> {
			if (!childrenEntityId2List.contains(getEntityId2.getEntityId2())) {
				childrenEntityId2List.add(getEntityId2.getEntityId2());
			}
		});
		if (!individualEntityId2List.isEmpty() || !childrenEntityId2List.isEmpty()
				|| !commercialEntityId2List.isEmpty()) {
			return checkEntityIdsList(entityId, relationList, owner, owned, affilated, subsidary, affialiateDataList,
					individualEntityId2List, commercialEntityId2List, childrenEntityId2List, children, parents);
		} else {
			child.addAll(children);
			child.addAll(parents);
		}
		return child;
	}

	/**
	 * @param entityId
	 * @param relationList
	 * @param owner
	 * @param owned
	 * @param affilated
	 * @param subsidary
	 * @param affialiateDataList
	 * @param individualEntityId2List
	 * @param commercialEntityId2List
	 * @param childrenEntityId2List
	 * @param children
	 * @param parents
	 * @return
	 */
	private List<EntityRelationshipType> checkEntityIdsList(String entityId, List<EntityRelationshipType> relationList,
			LosConfigDetails owner, LosConfigDetails owned, LosConfigDetails affilated, LosConfigDetails subsidary,
			List<EntityRelationshipType> affialiateDataList, List<String> individualEntityId2List,
			List<String> commercialEntityId2List, List<String> childrenEntityId2List,
			List<EntityRelationshipType> children, List<EntityRelationshipType> parents) {
		if (!childrenEntityId2List.isEmpty()) {
			listAPILevelTwoData(entityId, relationList, subsidary, childrenEntityId2List);
		}
		if (!individualEntityId2List.isEmpty()) {
			listAPIgetOwnedCompanies(entityId, relationList, affilated, affialiateDataList, individualEntityId2List,
					childrenEntityId2List);
		}
		parents.stream().forEach(changingRelation -> {
			changeRelationshipListApi(relationList, owner, owned, subsidary, changingRelation);
		});
		if (!commercialEntityId2List.isEmpty()) {
			parents.stream().forEach(changingRelation -> {
				if (!commercialEntityId2List.contains(changingRelation.getEntityId2()) && (changingRelation
						.getEntityId1().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
						&& changingRelation.getEntityId2().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE))) {
					List<EntityRelationshipType> levelOnesChildren = entityRelationshipTypeRepository
							.findByEntityId1AndDeletedAndStatus(commercialEntityId2List);
					relationList.addAll(levelOnesChildren);
				}
			});
		}
		return children;
	}

	/**
	 * @param entityId
	 * @param relationList
	 * @param affilated
	 * @param affialiateDataList
	 * @param individualEntityId2List
	 * @param childrenEntityId2List
	 */
	private void listAPIgetOwnedCompanies(String entityId, List<EntityRelationshipType> relationList,
			LosConfigDetails affilated, List<EntityRelationshipType> affialiateDataList,
			List<String> individualEntityId2List, List<String> childrenEntityId2List) {
		List<EntityRelationshipType> getOwnedCompanies = entityRelationshipTypeRepository
				.findByEntityId1AndDeletedREverse(individualEntityId2List);
		CommercialEntity parentEntity = new CommercialEntity();
		getOwnedCompanies.stream().forEach(changeRelation -> {
			if (!childrenEntityId2List.contains(changeRelation.getEntityId1())) {
				changeTheRelationDataOfOwnedCompanies(entityId, affilated, affialiateDataList, parentEntity,
						changeRelation);
			}
		});
		relationList.addAll(affialiateDataList);
	}

	/**
	 * @param entityId
	 * @param relationList
	 * @param subsidary
	 * @param childrenEntityId2List
	 */
	private void listAPILevelTwoData(String entityId, List<EntityRelationshipType> relationList,
			LosConfigDetails subsidary, List<String> childrenEntityId2List) {
		List<EntityRelationshipType> getSubsidaryRelation = entityRelationshipTypeRepository
				.findByEntityId2CAndEnityId1CInreverse(childrenEntityId2List);
		getSubsidaryRelation.stream().forEach(changeRelation -> {
			if (!changeRelation.getEntityId2().equals(entityId)) {
				if ((changeRelation.getEntityId1().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
						&& changeRelation.getEntityId2().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE))
						&& (getRelationTypeId(changeRelation).equals(LOSEntityConstants.OWNER)
								|| getRelationTypeId(changeRelation).equals(LOSEntityConstants.SUBSIDIARY))) {
					changeRelation.setEntityRelationConfigDetail(subsidary);
					relationList.add(changeRelation);
				}
			}
		});
		childrenEntityId2List.forEach(getchild -> {
			if (getchild.endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)) {
				List<EntityRelationshipType> getOtherRelation = entityRelationshipTypeRepository
						.findByEntityId1AndDeletedAndStatus(Arrays.asList(getchild));
				getOtherRelation.stream().forEach(getAffiliateData -> {
					if (!getRelationTypeId(getAffiliateData).equals(LOSEntityConstants.OWNER)
							|| !getRelationTypeId(getAffiliateData).equals(LOSEntityConstants.SUBSIDIARY)) {
						relationList.add(getAffiliateData);
					}
				});
			}
		});
	}

	/**
	 * Method to change TheRelationDataOfOwnedCompanies for the entityIds
	 * 
	 * @param entityId
	 * @param affilated
	 * @param affialiateDataList
	 * @param parentEntity
	 * @param changeRelation
	 */
	private void changeTheRelationDataOfOwnedCompanies(String entityId, LosConfigDetails affilated,
			List<EntityRelationshipType> affialiateDataList, CommercialEntity parentEntity,
			EntityRelationshipType changeRelation) {
		if (changeRelation.getEntityId1().equals(entityId)) {
			BeanUtils.copyProperties(changeRelation.getParentEntity(), parentEntity);
		}
		if (!changeRelation.getEntityId1().equals(entityId)
				&& changeRelation.getEntityId1().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& getRelationTypeId(changeRelation).equals(LOSEntityConstants.OWNED)) {
			changeRelation.setEntityId2(changeRelation.getEntityId1());
			changeRelation.setChildEntity(changeRelation.getParentEntity());
			changeRelation.setEntityId1(entityId);
			changeRelation.setParentEntity(parentEntity);
			changeRelation.setEntityRelationConfigDetail(affilated);
			affialiateDataList.add(changeRelation);
		}
	}

	/**
	 * Method to change the Relationship for list Api for changing the relation.
	 * 
	 * @param relationList
	 * @param owner
	 * @param owned
	 * @param subsidary
	 * @param changingRelation
	 */
	private void changeRelationshipListApi(List<EntityRelationshipType> relationList, LosConfigDetails owner,
			LosConfigDetails owned, LosConfigDetails subsidary, EntityRelationshipType changingRelation) {
		Long relationTypeId = getRelationTypeId(changingRelation);
		if (changingRelation.getEntityId1().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& changingRelation.getEntityId1().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& relationTypeId.equals(LOSEntityConstants.OWNER)) {
			changingRelation.setEntityRelationConfigDetail(subsidary);
		} else if (relationTypeId.equals(LOSEntityConstants.OWNER)) {
			changingRelation.setEntityRelationConfigDetail(owned);
		} else if (relationTypeId.equals(LOSEntityConstants.OWNED)) {
			changingRelation.setEntityRelationConfigDetail(owner);
		} else if (relationTypeId.equals(LOSEntityConstants.SUBSIDIARY)) {
			changingRelation.setEntityRelationConfigDetail(owner);
		}
		relationList.add(changingRelation);
	}

	/**
	 * Method to find the Data if parentNotPresent for Given entityId
	 * 
	 * @param entityId
	 * @param relationList
	 * @param owner
	 * @param affilated
	 * @param subsidary
	 * @param commercialEntityId2List
	 * @param cToCRelation
	 * @param iToCRelation
	 * @param parentEntity
	 * @param parents
	 */
	private void parentNotPresent(String entityId, List<EntityRelationshipType> relationList, LosConfigDetails owner,
			LosConfigDetails affilated, LosConfigDetails subsidary, List<String> commercialEntityId2List,
			List<String> cToCRelation, List<String> iToCRelation, CommercialEntity parentEntity,
			List<EntityRelationshipType> parents) {
		parents.forEach(parentsEntityId2 -> {
			getLevelOneEntityData(entityId, commercialEntityId2List, cToCRelation, parentEntity, parentsEntityId2);
		});
		if (!commercialEntityId2List.isEmpty()) {
			listAPILevelTwoData(entityId, relationList, subsidary, commercialEntityId2List);
		}
		if (!cToCRelation.isEmpty()) {
			List<EntityRelationshipType> getOwners = entityRelationshipTypeRepository
					.findByEntityId2CAndEnityId1IInReverse(cToCRelation);
			getOwners.stream().forEach(changeRelation -> {
				if (getRelationTypeId(changeRelation).equals(LOSEntityConstants.OWNED)) {
					iToCRelation.add(changeRelation.getEntityId2());
					changeRelation.setEntityRelationConfigDetail(owner);
					relationList.add(changeRelation);
				}
			});
			List<EntityRelationshipType> getOtherRelation = entityRelationshipTypeRepository
					.findByEntityId1AndDeletedAndStatus(cToCRelation);
			if (!getOtherRelation.isEmpty()) {
				relationList.addAll(getOtherRelation);
			}
		}
		if (!iToCRelation.isEmpty()) {
			List<EntityRelationshipType> getOwnersOtherCompanies = entityRelationshipTypeRepository
					.findByEntityId1IAndReturnCListInreverse(iToCRelation);
			getOwnersOtherCompanies.stream().forEach(changeRelation -> {
				usingLevelThreeDataChangingToAffiliated(entityId, relationList, affilated, cToCRelation, parentEntity,
						changeRelation);
			});
		}
	}

	/**
	 * By using the level 3 data and finding the the Owned companies of Individual
	 * Entities making Affiliated
	 * 
	 * @param entityId
	 * @param relationList
	 * @param affilated
	 * @param cToCRelation
	 * @param parentEntity
	 * @param changeRelation
	 */
	private void usingLevelThreeDataChangingToAffiliated(String entityId, List<EntityRelationshipType> relationList,
			LosConfigDetails affilated, List<String> cToCRelation, CommercialEntity parentEntity,
			EntityRelationshipType changeRelation) {
		if (!changeRelation.getEntityId1().equals(entityId) && !cToCRelation.contains(changeRelation.getEntityId1())
				&& getRelationTypeId(changeRelation).equals(LOSEntityConstants.OWNED)) {
			changeRelation.setEntityId2(changeRelation.getEntityId1());
			changeRelation.setChildEntity(changeRelation.getParentEntity());
			changeRelation.setEntityId1(entityId);
			changeRelation.setParentEntity(parentEntity);
			changeRelation.setEntityRelationConfigDetail(affilated);
			relationList.add(changeRelation);
		}
	}

	/**
	 * Method to getLevelOneEntityData
	 * 
	 * @param entityId
	 * @param commercialEntityId2List
	 * @param cToCRelation
	 * @param parentEntity
	 * @param parentsEntityId2
	 */
	private void getLevelOneEntityData(String entityId, List<String> commercialEntityId2List, List<String> cToCRelation,
			CommercialEntity parentEntity, EntityRelationshipType parentsEntityId2) {
		if (parentsEntityId2.getEntityId1().endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
				&& parentsEntityId2.getEntityId2().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				|| parentsEntityId2.getEntityId1().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
						&& parentsEntityId2.getEntityId2().endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
				|| parentsEntityId2.getEntityId1().endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
						&& parentsEntityId2.getEntityId2().endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)) {
			commercialEntityId2List.add(parentsEntityId2.getEntityId2());
		} else if (parentsEntityId2.getEntityId1().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& parentsEntityId2.getEntityId2().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)) {
			cToCRelation.add(parentsEntityId2.getEntityId2());
			if (parentsEntityId2.getEntityId1().equals(entityId)) {
				BeanUtils.copyProperties(parentsEntityId2.getParentEntity(), parentEntity);
			}
		}
	}

	/**
	 * Method to return the RelationTypeId
	 * 
	 * @param changingRelation
	 * @return
	 */
	private Long getRelationTypeId(EntityRelationshipType changingRelation) {
		return changingRelation.getEntityRelationConfigDetail().getConfigId();
	}

	/**
	 * Method updateEntityRelaton
	 */
	@Override
	public Long updateEntityRelaton(Long id, EntityRelationshipType entityRelationshipType) {
		LOG.debug("updating EntityRelaton");
		clientValidator.isValidClient(entityRelationshipType);
		EntityRelationshipType relation = entityRelationshipTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Entity relationshiptype id not found: " + id));
		if (relation.isDeleted() == Boolean.TRUE) {
			errorMsgs.getMessage(INVALID_RELATIONTYPE_ID, new Object[] { id }, CommonUtils.getLocale());
		}
		if (entityRelationshipType.getEntityRelationConfigDetail() == null) {
			throw new BadRequestException("Please provide entityRelationConfigDetail");
		}
		if (getEntityRelationId(entityRelationshipType) == 0 || getEntityRelationId(entityRelationshipType) == null) {
			throw new BadRequestException("configId Cannot be 0 or Null");
		}
		if (entityRelationshipType.getOwnership() == null) {
			throw new BadRequestException("Please Provide Ownership Details");
		}
		LosConfigDetails entityRelationConfigDetails = configLookupRepository
				.findById(getEntityRelationId(entityRelationshipType)).orElseThrow(() -> new BadRequestException(
						errorMsgs.getMessage(INVALID_RELATION_TYPE_ID, null, CommonUtils.getLocale())));
		if (!entityRelationConfigDetails.getConfigtypeCode().equals(LOSEntityConstants.ENTITY_RELATION_TYPE_CODE)) {
			throw new BadRequestException(
					errorMsgs.getMessage(INVALID_RELATION_TYPE_ID, null, CommonUtils.getLocale()));
		}
		if (entityRelationshipType.getEntityId2().equals(relation.getEntityId2())
				&& getRelationTypeId(entityRelationshipType).equals(getRelationTypeId(relation))) {
			entityRelationshipValidator.validateEntityRelationCombination(relation.getEntityId1(),
					entityRelationshipType);
			mapValuesUpdateRelationship(entityRelationshipType, relation);
			return entityRelationshipTypeRepository.save(entityRelationshipType).getRelationshiptypeId();
		} else {
			return updateReverseRelation(entityRelationshipType, relation);
		}
	}

	/**
	 * updateReverseRelation
	 * 
	 * @param entityRelationshipType
	 * @param relation
	 * @return
	 */
	private Long updateReverseRelation(EntityRelationshipType entityRelationshipType, EntityRelationshipType relation) {
		entityRelationshipValidator.validateEntityRelationCombination(relation.getEntityId2(), entityRelationshipType);
		mapValuesUpdateRelationship(entityRelationshipType, relation);
		if (relation.getEntityId1().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
				&& relation.getEntityId2().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)) {
			if (!entityRelationshipType.getRelationshiptypeId().equals(LOSEntityConstants.AFFILIATED)) {
				if (entityRelationshipType.getRelationshiptypeId().equals(LOSEntityConstants.SUBSIDIARY)
						&& relation.getRelationshiptypeId().equals(LOSEntityConstants.SUBSIDIARY)) {
					entityRelationshipType.setRelationshiptypeId(LOSEntityConstants.OWNER);
				} else if (entityRelationshipType.getRelationshiptypeId().equals(LOSEntityConstants.OWNER)
						&& relation.getRelationshiptypeId().equals(LOSEntityConstants.OWNER)
						|| entityRelationshipType.getRelationshiptypeId().equals(LOSEntityConstants.SUBSIDIARY)
								&& relation.getRelationshiptypeId().equals(LOSEntityConstants.OWNER)) {
					entityRelationshipType.setRelationshiptypeId(LOSEntityConstants.SUBSIDIARY);
				}
			}
			return entityRelationshipTypeRepository.save(entityRelationshipType).getRelationshiptypeId();
		} else {
			entityRelationshipType.setRelationshiptypeId(relation.getRelationshiptypeId());
			return entityRelationshipTypeRepository.save(entityRelationshipType).getRelationshiptypeId();
		}
	}

	/**
	 * method to Map Values for UpdateRelationship
	 * 
	 * @param entityRelationshipType
	 * @param relation
	 */
	private void mapValuesUpdateRelationship(EntityRelationshipType entityRelationshipType,
			EntityRelationshipType relation) {
		entityRelationshipType.setEntityRelationshiptypeId(relation.getEntityRelationshiptypeId());
		entityRelationshipType.setEntityId1(relation.getEntityId1());
		entityRelationshipType.setEntityId2(relation.getEntityId2());
	}

	/**
	 * Method to Deactivate EntityRelationship
	 */
	@Override
	public void deactivateEntityRelationship(Long id) {
		entityRelationshipTypeRepository.deactivateEntityRelationship(id);
	}

	/**
	 * Method to Activate EntityRelationship
	 */
	@Override
	public void activateEntityRelationship(Long id) {
		entityRelationshipTypeRepository.activateEntityRelationship(id);
	}

	/**
	 * Method to Find the children count of given entityId
	 */
	@Override
	public Long findChildEntityCount(String entityId) {
		return parentEntityNodeRepository.countByParentIdAndActive(entityId, true);
	}

	/**
	 * Method to Find the parents count of given entityId
	 */
	@Override
	public Long findParentEntityCount(String entityId) {
		return childEntityNodeRepository.countByParentIdAndActive(entityId, true);
	}

	/**
	 * Method to Get Entity Relationship Tree
	 * <p>
	 * Method findEntityRelationTreeByParentEntityId based on given entity check the
	 * type of relation. checks: 1.commercial to commercial owner relation if not
	 * present 2. The given entity is having parent(if present parentCount==0) or
	 * not (if present parentCount > 0) finally it forms the tree using
	 * createTreeFromParent method and return the tree data
	 * </p>
	 */
	@Override
	public ParentEntityNode findEntityRelationTreeByParentEntityId(String entityId) {
		LosConfigDetails owner = configLookupRepository.findByConfigId(LOSEntityConstants.OWNER);
		LosConfigDetails owned = configLookupRepository.findByConfigId(LOSEntityConstants.OWNED);
		LosConfigDetails affilated = configLookupRepository.findByConfigId(LOSEntityConstants.AFFILIATED);
		LosConfigDetails subsidary = configLookupRepository.findByConfigId(LOSEntityConstants.SUBSIDIARY);
		String commercialSuffix = LOSEntityConstants.COMMERCIAL_SUFFIX_CODE;
		Long parentCount = findParentEntityCount(entityId);
		List<EntityRelationshipType> relationDetails = entityRelationshipTypeRepository
				.findByEntityId1AndDeleted(Arrays.asList(entityId));
		if (!relationDetails.isEmpty()) {
			List<String> iterationChildIds = new ArrayList<>();
			List<EntityRelationshipType> isOwnerRelation = relationDetails.stream().filter(
					checkRelationType -> getEntityRelationId(checkRelationType).equals(LOSEntityConstants.OWNER))
					.collect(Collectors.toList());
			if (!isOwnerRelation.isEmpty()) {
				List<String> entityIds = new ArrayList<>();
				List<ParentEntityNode> treeData = new ArrayList<>();
				List<String> levelTwoChildIds = new ArrayList<>();
				for (EntityRelationshipType eachRelation : relationDetails) {
					if (getEntityRelationId(eachRelation).equals(LOSEntityConstants.OWNER)
							&& eachRelation.getEntityId1().endsWith(commercialSuffix)
							&& eachRelation.getEntityId2().endsWith(commercialSuffix)) {
						entityIds.add(eachRelation.getEntityId2());
						List<ParentEntityNode> rootData = parentEntityNodeRepository.findEntityTreeData(entityId);
						treeData.addAll(rootData);
					}
					if (!entityIds.isEmpty()) {
						return cToCOwnerCheckLeveOneHavingParentOrChildren(entityId, owner, affilated, commercialSuffix, // NOSONAR
								iterationChildIds, entityIds, treeData, levelTwoChildIds);// NOSONAR
					}
				}
			}
		}
		if (parentCount == 0) {
			List<ParentEntityNode> treeData = entityParentIsNotPresent(entityId, subsidary, affilated, owner,
					commercialSuffix);
			return entityRelationshipTreeServiceImpl.createTreeFromParent(treeData, entityId);
		} else if (parentCount > 0) {
			List<ParentEntityNode> treeData = entityParentIsPresent(entityId, owner, owned, affilated, subsidary,
					commercialSuffix);
			return entityRelationshipTreeServiceImpl.createTreeFromParent(treeData, entityId);
		}
		List<ParentEntityNode> parentEntityNode = parentEntityNodeRepository.findEntityTreeData(entityId);
		return entityRelationshipTreeServiceImpl.createTreeFromParent(parentEntityNode, entityId);
	}

	/**
	 * <p>
	 * In this method Commercial to commercial having Owner Relation for the given
	 * entity. given entity having the child then get those (commercial entity)
	 * child if present goes to levelTwoCToCOwnerGetComEntitySetAsAffiliated(method)
	 * after that it returns the tree data.
	 * </p>
	 * 
	 * @param entityId
	 * @param owner
	 * @param affilated
	 * @param commercialSuffix
	 * @param iterationChildIds
	 * @param entityIds
	 * @param treeData
	 * @param levelTwoChildIds
	 * @return
	 */
	private ParentEntityNode cToCOwnerCheckLeveOneHavingParentOrChildren(String entityId, LosConfigDetails owner, // NOSONAR
			LosConfigDetails affilated, String commercialSuffix, List<String> iterationChildIds, List<String> entityIds, // NOSONAR
			List<ParentEntityNode> treeData, List<String> levelTwoChildIds) {// NOSONAR
		List<ParentEntityNode> levelOneData = getParentDataReverseOrder(entityIds);
		levelOneData.stream().forEach(removeExisting -> cToCOwnerIfOwnedIsPresentChangeToOwner(entityId, owner,
				treeData, levelTwoChildIds, removeExisting));
		if (!levelTwoChildIds.isEmpty()) {
			levelTwoCToCOwnerGetComEntitySetAsAffiliated(entityId, owner, affilated, commercialSuffix, // NOSONAR
					iterationChildIds, entityIds, treeData, levelTwoChildIds);// NOSONAR
		}
		return entityRelationshipTreeServiceImpl.createTreeFromParent(treeData, entityId);
	}

	/**
	 * <p>
	 * In this method Commercial to commercial having Owner Relation for the given
	 * entity is true, (method) cToCOwnerCheckLeveOneHavingParentOrChildren calls
	 * this method if level one(commercial entity) if this entity is owned by other
	 * individual entity then change to owner relation and collects their child
	 * which not present in tree data if any commercial entity is found make it as
	 * affiliated to the root node(rootEntity/or 0th level entity) and get their
	 * parent if present add it as owner for that affiliated entity's
	 * </p>
	 * 
	 * @param entityId
	 * @param owner
	 * @param affilated
	 * @param commercialSuffix
	 * @param iterationChildIds
	 * @param entityIds
	 * @param treeData
	 * @param levelTwoChildIds
	 */
	private void levelTwoCToCOwnerGetComEntitySetAsAffiliated(String entityId, LosConfigDetails owner, // NOSONAR
			LosConfigDetails affilated, String commercialSuffix, List<String> iterationChildIds, List<String> entityIds, // NOSONAR
			List<ParentEntityNode> treeData, List<String> levelTwoChildIds) { // NOSONAR
		List<ParentEntityNode> levelTwoData = parentEntityNodeRepository.findChildEntityData(levelTwoChildIds);
		if (!levelTwoData.isEmpty()) {
			levelTwoData.stream().forEach(entites -> {
				if (!entityIds.contains(entites.getChildId()) && !levelTwoChildIds.contains(entites.getChildId())
						&& !iterationChildIds.contains(entites.getChildId())
						&& getConfigId(entites).equals(LOSEntityConstants.OWNED)) {
					iterationChildIds.add(entites.getChildId());
					entites.setParentId(entityId);
					if (entites.getChildId().endsWith(commercialSuffix)) {
						entites.setLosConfigDetails(affilated);
					}
					List<ParentEntityNode> levelThreeData = getParentDataReverseOrder(
							Arrays.asList(entites.getChildId()));
					levelThreeData.stream().forEach(setChildrenNull -> {
						if (getConfigId(setChildrenNull).equals(LOSEntityConstants.OWNED)) {
							setChildrenNull.setLosConfigDetails(owner);
							setChildrenNull.setChildren(Arrays.asList());
							treeData.add(setChildrenNull);
						}
					});
					treeData.add(entites);
				}
			});
		}
	}

	/**
	 * <p>
	 * Method validates the given EntityId should not present in between the tree
	 * data. if relation is having the Owned then changing that relation into Owner
	 * and set their children to empty List.
	 * </p>
	 * 
	 * @param entityId
	 * @param owner
	 * @param treeData
	 * @param levelTwoChildIds
	 * @param removeExisting
	 */
	private void cToCOwnerIfOwnedIsPresentChangeToOwner(String entityId, LosConfigDetails owner,
			List<ParentEntityNode> treeData, List<String> levelTwoChildIds, ParentEntityNode removeExisting) {
		if (!removeExisting.getChildId().equals(entityId)) {
			levelTwoChildIds.add(removeExisting.getChildId());
			if (getConfigId(removeExisting).equals(LOSEntityConstants.OWNED)) {
				removeExisting.setLosConfigDetails(owner);
				removeExisting.setChildren(Arrays.asList());
			}
			treeData.add(removeExisting);
		}
	}

	/**
	 * Method called by findEntityRelationTreeByParentEntityId(method)
	 * <p>
	 * if given entityParentIsNotPresent collect their child entityIds if individual
	 * entityId is present set their children as emptyList and if the child
	 * enittyIds present then pass those childIds to
	 * entityParentIsNotPresentGetFirstLevelsChildren(method) after the method is
	 * performed then add to the treeData and return the treeData
	 * </p>
	 * 
	 * @param entityId
	 * @param subsidary
	 * @param commercialSuffix
	 * @return
	 */
	private List<ParentEntityNode> entityParentIsNotPresent(String entityId, LosConfigDetails subsidary,
			LosConfigDetails affilated, LosConfigDetails owner, String commercialSuffix) {
		List<ParentEntityNode> treeData = new ArrayList<>();
		List<String> levelTwoChildIds = new ArrayList<>();
		List<ParentEntityNode> parentEntityNode = parentEntityNodeRepository
				.findChildEntityData(Arrays.asList(entityId));
		if (!parentEntityNode.isEmpty()) {
			List<String> childIds = new ArrayList<>();
			List<String> individualChildIds = new ArrayList<>();
			parentEntityNode.stream().forEach(entity -> {
				if (entity.getChildId().endsWith(commercialSuffix)) {
					childIds.add(entity.getChildId());
				} else {
					individualChildIds.add(entity.getChildId());
					entity.setChildren(Arrays.asList());
				}
			});
			if (!childIds.isEmpty()) {
				entityParentIsNotPresentGetFirstLevelsChildren(subsidary, commercialSuffix, treeData, childIds);
			}
			if (!individualChildIds.isEmpty()) {
				List<ParentEntityNode> levelOneData = parentEntityNodeRepository
						.findChildEntityData(individualChildIds);
				if (!levelOneData.isEmpty()) {
					levelOneData.stream().forEach(entites -> {
						if (!individualChildIds.contains(entites.getChildId())
								&& !levelTwoChildIds.contains(entites.getChildId())
								&& entityId.endsWith(commercialSuffix)
								&& getConfigId(entites).equals(LOSEntityConstants.OWNED)) {
							entites.setParentId(entityId);
							if (entites.getChildId().endsWith(commercialSuffix)) {
								entites.setLosConfigDetails(affilated);
								entites.setChildren(Arrays.asList());
								parentEntityNode.add(entites);
							}
						}
					});
				}
			}
		}
		treeData.addAll(parentEntityNode);
		return treeData;
	}

	/**
	 * <p>
	 * This method is called by entityParentIsNotPresent(method), getting the
	 * levelOne entityIds and and iterating to one more level and iterating and
	 * changing the relation having Owned to subsidary and adding that data to
	 * treeData list.
	 * </p>
	 * 
	 * @param subsidary
	 * @param commercialSuffix
	 * @param treeData
	 * @param childIds
	 */
	private void entityParentIsNotPresentGetFirstLevelsChildren(LosConfigDetails subsidary, String commercialSuffix,
			List<ParentEntityNode> treeData, List<String> childIds) {
		List<ParentEntityNode> levelOneData = getParentDataReverseOrder(childIds);
		if (!levelOneData.isEmpty()) {
			List<ParentEntityNode> list = levelOneData.stream()
					.filter(getRelation -> getConfigId(getRelation).equals(LOSEntityConstants.OWNER)
							&& getRelation.getChildId().endsWith(commercialSuffix))
					.collect(Collectors.toList());
			if (!list.isEmpty()) {
				for (ParentEntityNode eachParentEntityNode : list) {
					eachParentEntityNode.setLosConfigDetails(subsidary);
					eachParentEntityNode.setChildren(Arrays.asList());
					treeData.add(eachParentEntityNode);
				}
			}
		}
	}

	/**
	 * Method called by findEntityRelationTreeByParentEntityId(method)
	 * <p>
	 * if given entityParentIsPresent collect their child entityIds if individual
	 * entityId is present set their children as emptyList and if the child
	 * enittyIds present then pass those childIds to
	 * getParentDataReverseOrder(method) to get the reverse value parent-child to
	 * child-parent after the method is performed then add to the treeData again if
	 * levelTwo has children iterate it again then add to treeData return the
	 * treeData
	 * </p>
	 * 
	 * @param entityId
	 * @param owner
	 * @param owned
	 * @param affilated
	 * @param subsidary
	 * @param commercialSuffix
	 * @return
	 */
	private List<ParentEntityNode> entityParentIsPresent(String entityId, LosConfigDetails owner,
			LosConfigDetails owned, LosConfigDetails affilated, LosConfigDetails subsidary, String commercialSuffix) {
		List<String> childIds = new ArrayList<>();
		List<String> iterationChildIdS = new ArrayList<>();
		List<String> levelTwoChildIds = new ArrayList<>();
		List<ParentEntityNode> treeData = parentEntityNodeRepository.findEntityTreeData(entityId);
		treeData.stream().forEach(clids -> {
			if (clids.getChildId().endsWith(commercialSuffix)) {
				levelTwoChildIds.add(clids.getChildId());
			}
		});
		if (!levelTwoChildIds.isEmpty()) {
			List<ParentEntityNode> getParents = getParentDataReverseOrder(levelTwoChildIds);
			getParents.stream().forEach(findCtoCRel -> {
				if (findCtoCRel.getChildId().endsWith(commercialSuffix)
						&& getConfigId(findCtoCRel).equals(LOSEntityConstants.OWNER)) {
					findCtoCRel.setLosConfigDetails(subsidary);
					findCtoCRel.setChildren(Arrays.asList());
					treeData.add(findCtoCRel);
				} else {
					findCtoCRel.setChildren(Arrays.asList());
				}
			});
		}
		List<ParentEntityNode> parentData = getParentDataReverseOrder(Arrays.asList(entityId));
		parentData.stream()
				.forEach(parent -> changingRelations(owner, owned, subsidary, commercialSuffix, childIds, parent));
		if (entityId.endsWith(commercialSuffix)) {
			entityParentIsPresentSecondLeveIteration(entityId, owner, affilated, subsidary, commercialSuffix, childIds,
					iterationChildIdS, treeData);
		}
		treeData.addAll(parentData);
		return treeData;
	}

	/**
	 * This method is called by entityParentIsPresent(method), getting the levelOne
	 * entityIds and and iterating to one more level and changing the relation
	 * having Owned to Owner and adding that data to ParentEntityNode list.
	 * 
	 * @param entityId
	 * @param owner
	 * @param affilated
	 * @param subsidary
	 * @param commercialSuffix
	 * @param childIds
	 * @param iterationChildIdS
	 * @param parentEntityNode
	 */
	private void entityParentIsPresentSecondLeveIteration(String entityId, LosConfigDetails owner,
			LosConfigDetails affilated, LosConfigDetails subsidary, String commercialSuffix, List<String> childIds,
			List<String> iterationChildIdS, List<ParentEntityNode> parentEntityNode) {
		List<ParentEntityNode> secondIteration = parentEntityNodeRepository.findChildEntityData(childIds);
		if (!secondIteration.isEmpty()) {
			secondIteration.stream().forEach(iteration -> {
				if (!iteration.getChildId().equals(entityId) && iteration.getChildId().endsWith(commercialSuffix)
						&& !iterationChildIdS.contains(iteration.getChildId())) {
					iterationChildIdS.add(iteration.getChildId());
					iteration.setParentId(entityId);
					iteration.setLosConfigDetails(affilated);
					List<ParentEntityNode> childOfChild = getParentDataReverseOrder(
							Arrays.asList(iteration.getChildId()));
					if (!childOfChild.isEmpty()) {
						childOfChild.stream().forEach(setParentAsChild -> {
							if (setParentAsChild.getChildId().endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
									&& getConfigId(setParentAsChild).equals(LOSEntityConstants.OWNED)) {
								setParentAsChild.setLosConfigDetails(owner);
								setParentAsChild.setChildren(Arrays.asList());
								parentEntityNode.add(setParentAsChild);
							} else if (setParentAsChild.getChildId().endsWith(LOSEntityConstants.COMMERCIAL_SUFFIX_CODE)
									&& getConfigId(setParentAsChild).equals(LOSEntityConstants.OWNER)) {
								setParentAsChild.setLosConfigDetails(subsidary);
								setParentAsChild.setChildren(Arrays.asList());
								parentEntityNode.add(setParentAsChild);
							}
						});
					}
					parentEntityNode.add(iteration);
				}
			});
		}
		if (entityId.endsWith(commercialSuffix)) {
			List<ParentEntityNode> getParent = getParentDataReverseOrder(childIds);
			if (!getParent.isEmpty()) {
				getParent.stream().forEach(changeRelation -> {
					if (getConfigId(changeRelation).equals(LOSEntityConstants.OWNED)) {
						changeRelation.setLosConfigDetails(owner);
						changeRelation.setChildren(Arrays.asList());
						parentEntityNode.add(changeRelation);
					} else if (changeRelation.getParentId().endsWith(LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE)
							&& getConfigId(changeRelation).equals(LOSEntityConstants.OWNER)) {
						changeRelation.setParentId(entityId);
						changeRelation.setLosConfigDetails(affilated);
						changeRelation.setChildren(Arrays.asList());
						parentEntityNode.add(changeRelation);
					}

				});
			}
		}
	}

	/**
	 * <p>
	 * After getting the reversed entities the relationship between the entities
	 * will change based on relation as follows: if entityId1 and entityId2 both are
	 * commercial and having Owner Relation then it changes Owner to subsidary.
	 * Other relation changes are if Owner - Owned ,Owned -Owner ,Owner - Subsidary,
	 * 
	 * </p>
	 * 
	 * @param owner
	 * @param owned
	 * @param subsidary
	 * @param commercialSuffix
	 * @param childIds
	 * @param parent
	 */
	private void changingRelations(LosConfigDetails owner, LosConfigDetails owned, LosConfigDetails subsidary,
			String commercialSuffix, List<String> childIds, ParentEntityNode parent) {
		parent.setChildren(Arrays.asList());
		childIds.add(parent.getChildId());
		Long relationId = getConfigId(parent);
		if ((long) relationId == LOSEntityConstants.OWNER && parent.getParentId().endsWith(commercialSuffix)
				&& parent.getChildId().endsWith(commercialSuffix)) {
			parent.setLosConfigDetails(subsidary);
		} else if ((long) relationId == LOSEntityConstants.OWNER) {
			parent.setLosConfigDetails(owned);
		} else if ((long) relationId == LOSEntityConstants.OWNED) {
			parent.setLosConfigDetails(owner);
		} else if ((long) relationId == LOSEntityConstants.SUBSIDIARY) {
			parent.setLosConfigDetails(owner);
		}
	}

	/**
	 * Method returns the RelationshipId from ParentEntityNode
	 * 
	 * @param getRelationId
	 * @return
	 */
	private Long getConfigId(ParentEntityNode getRelationId) {
		return getRelationId.getLosConfigDetails().getConfigId();
	}

	/**
	 * Method returns the RelationshipId from EntityRelationshipType
	 * 
	 * @param eachRelation
	 * @return
	 */
	private Long getEntityRelationId(EntityRelationshipType eachRelation) {
		return getRelationTypeId(eachRelation);
	}

	/**
	 * Method returns the list ParentEntityNode by replacing the EntityId1 and
	 * EntityId2 to EntityId2 and EntityId1
	 * 
	 * @param entityIds
	 * @return
	 */
	private List<ParentEntityNode> getParentDataReverseOrder(List<String> entityIds) {
		return parentEntityNodeRepository.findParentEntityTreeData(entityIds);
	}

	/**
	 * Method used to Delete the Relationship by RelationId
	 */
	@Override
	public void deleteEntityRelation(Long id) {
		EntityRelationshipType entRelationshipType = entityRelationshipTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Entity relationshiptype id not found: " + id));
		if (entRelationshipType != null && entRelationshipType.isDeleted() == Boolean.TRUE) {
			LOG.info(ActivityLog.getActivityLog(null, Operation.DELETE, String.valueOf(id),
					"Invalid EntityRelation Id to Delete", Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
			throw new ResourceNotFoundException(
					errorMsgs.getMessage(INVALID_RELATIONTYPE_ID, new Object[] { id }, CommonUtils.getLocale()));
		}
		entityRelationshipTypeRepository.deleteEntityRelaton(id);
	}

	@Override
	public List<EntityRelationshipType> findEntityRelationsListByIds(List<String> entityIds) {
		List<EntityRelationshipType> relationList = entityRelationshipTypeRepository
				.findAllRelatedEntityRelationData(entityIds);
		List<EntityRelationshipType> removedPassedEntityIdList = new ArrayList<>();
		relationList.stream().forEach(er -> {
			if (!entityIds.contains(er.getEntityId2())) {
				removedPassedEntityIdList.add(er);
			}
		});
		return removedPassedEntityIdList;
	}

	@Override
	public EntityNodeSpouseDto findSpouse(String entityId) {
		// get Entity d1 or Entity Id
		EntityNodeSpouseDto entityNodeSpouseDto = new EntityNodeSpouseDto();
		Optional<EntityRelationshipType> entityRelationshipTypeOptional = entityRelationshipTypeRepository
				.findByEntityId1IOrEntityId2(entityId, LOSEntityConstants.SPOUSE);
		if (entityRelationshipTypeOptional.isPresent()) {
			EntityRelationshipType entityRelationshipType = entityRelationshipTypeOptional.get();
			String spouseEntityId = getSpouseEntityId(entityRelationshipType, entityId);
			Optional<EntityNodeSpouse> individualEntityOptional = individualEntityRepository.getSpouse(spouseEntityId);
			if (individualEntityOptional.isPresent()) {
				EntityNodeSpouse individualEntity = individualEntityOptional.get();
				entityNodeSpouseDto.setSpouseEntityID(individualEntity.getEntity_id());
				entityNodeSpouseDto.setFirstName(individualEntity.getFirst_name());
				entityNodeSpouseDto.setLastName(individualEntity.getLast_name());
			}
		}
		return entityNodeSpouseDto;
	}

	private String getSpouseEntityId(EntityRelationshipType relationshipType, String entityId) {
		if (relationshipType.getEntityId2().equals(entityId)) {
			return relationshipType.getEntityId1();
		}
		return relationshipType.getEntityId2();
	}

}
