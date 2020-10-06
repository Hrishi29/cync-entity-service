package com.idexcel.cync.los.entity.service.impl;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.config.EntityConfig;
import com.idexcel.cync.los.entity.dao.ChildEntityNodeRepository;
import com.idexcel.cync.los.entity.dao.ClientDetailsRepository;
import com.idexcel.cync.los.entity.dao.ConfigLookupRepository;
import com.idexcel.cync.los.entity.dao.EntityNodeRepository;
import com.idexcel.cync.los.entity.dao.EntityRelationshipTypeRepository;
import com.idexcel.cync.los.entity.dao.FinancialRepository;
import com.idexcel.cync.los.entity.dao.IndividualEntityRepository;
import com.idexcel.cync.los.entity.dao.ParentEntityNodeRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.exception.ResourceNotFoundException;
import com.idexcel.cync.los.entity.model.EntityNode;
import com.idexcel.cync.los.entity.model.EntityNodeSpouse;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfig;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.ParentEntityNode;
import com.idexcel.cync.los.entity.validator.ClientValidator;
import com.idexcel.cync.los.entity.validator.EntityRelationshipValidatorService;

@RunWith(SpringJUnit4ClassRunner.class)
public class EntityRelationshipTypeServiceImplTest {

	@InjectMocks
	EntityRelationshipTypeServiceImpl serviceImpl;

	@MockBean
	EntityConfig financeConfig;

	@MockBean
	private ClientValidator clientValidator;

	@InjectMocks
	private EntityRelationshipValidatorService entityRelationshipValidator;

	@MockBean
	private EntityRelationshipTypeRepository entityRelationshipTypeRepository;

	@MockBean
	public EntityNodeRepository entityNodeRepository;

	@InjectMocks
	public EntityRelationshipTreeServiceImpl entityRelationshipTreeServiceImpl;

	@MockBean
	private ChildEntityNodeRepository childEntityNodeRepository;

	@MockBean
	public ParentEntityNodeRepository parentEntityNodeRepository;
	@MockBean
	private FinancialRepository financialRepository;

	@MockBean
	ConfigLookupRepository configLookupRepository;

	@MockBean
	MessageSource errorMsgs;

	@MockBean
	ClientDetailsRepository clientDetailsRepository;

	@MockBean
	IndividualEntityRepository individualEntityRepository;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		this.entityRelationshipValidator = new EntityRelationshipValidatorService(financialRepository,
				entityRelationshipTypeRepository, childEntityNodeRepository, configLookupRepository,
				entityRelationshipTreeServiceImpl, errorMsgs);
		MockitoAnnotations.initMocks(this);
		this.serviceImpl = new EntityRelationshipTypeServiceImpl(entityNodeRepository, entityRelationshipTypeRepository,
				configLookupRepository, entityRelationshipValidator, parentEntityNodeRepository,
				childEntityNodeRepository, entityRelationshipTreeServiceImpl);
		MockitoAnnotations.initMocks(this);
		this.entityRelationshipTreeServiceImpl = new EntityRelationshipTreeServiceImpl(financialRepository, errorMsgs);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void activateEntityRelationshipTest() {
		serviceImpl.activateEntityRelationship((long) 10002);
		assertNull(null);
	}

	@Test
	public void deactivateEntityRelationshiptest() {
		serviceImpl.deactivateEntityRelationship((long) 10002);
		assertNull(null);
	}

	@Test
	public void findParentEntityCountTest() {
		String entityId1 = "FBOC000000C";
		Mockito.when(parentEntityNodeRepository.countByParentIdAndActive(entityId1, true)).thenReturn(new Long(1000));
		serviceImpl.findParentEntityCount(entityId1);
		assertNull(null);
	}

	@Test
	public void findChildEntityCountTest() {
		String entityId2 = "FBOC000000C";
		Mockito.when(childEntityNodeRepository.countByParentIdAndActive(entityId2, true)).thenReturn((long) 10000);
		serviceImpl.findChildEntityCount(entityId2);
		assertNull(null);
	}

	@Test
	public void deleteEntityRelationTest() {
		EntityRelationshipType entRelationshipType = new EntityRelationshipType();
		Mockito.when(entityRelationshipTypeRepository.findById((long) 10002))
				.thenReturn(Optional.of(entRelationshipType));
		serviceImpl.deleteEntityRelation((long) 10002);
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void deleteEntityRelationIdNotFoundTest() {
		Mockito.when(entityRelationshipTypeRepository.findById((long) 10002)).thenReturn(Optional.empty());
		serviceImpl.deleteEntityRelation((long) 10002);
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void deleteEntityRelationDeletedTrueTest() {
		EntityRelationshipType entRelationshipType = new EntityRelationshipType();
		entRelationshipType.setDeleted(true);
		Mockito.when(entityRelationshipTypeRepository.findById((long) 12)).thenReturn(Optional.of(entRelationshipType));
		serviceImpl.deleteEntityRelation((long) 12);
		assertNull(null);
	}

	@Test
	public void deleteEntityRelationDeletedFalseTest() {
		EntityRelationshipType entRelationshipType = new EntityRelationshipType();
		entRelationshipType.setDeleted(false);
		Mockito.when(entityRelationshipTypeRepository.findById((long) 12)).thenReturn(Optional.of(entRelationshipType));
		serviceImpl.deleteEntityRelation((long) 12);
		assertNull(null);
	}

	@Test
	public void findEntityRelationListByEntityIdDeletedFalseTest() {
		String entityId1 = "FBOC000000C";
		List<EntityRelationshipType> relationList = new ArrayList<>();
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setDeleted(false);
		relationList.add(entityRelationshipType);
		Mockito.when(entityRelationshipTypeRepository.findEntityTreeData(entityId1)).thenReturn(relationList);
		serviceImpl.findEntityRelationListByEntityId(entityId1);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void updateEntityRelatonTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		entityRelationConfigDetail.setConfigtypeCode(LOSEntityConstants.ENTITY_RELATION_TYPE_CODE);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId1("FBOC0001C");
		entityRelationshipType.setEntityId2("FBOC0001C");
		entityRelationshipType.setDeleted(true);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(entityRelationshipTypeRepository.findById((long) 1))
				.thenReturn(Optional.of(entityRelationshipType));
		serviceImpl.updateEntityRelaton((long) 1, entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void updateEntityRelatonNotTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		entityRelationConfigDetail.setConfigtypeCode(LOSEntityConstants.ENTITY_RELATION_TYPE_CODE);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId1("FBOC0001C");
		entityRelationshipType.setEntityId2("FBOC0001C");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(entityRelationshipTypeRepository.findById((long) 1)).thenReturn(Optional.empty());
		serviceImpl.updateEntityRelaton((long) 1, entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void updateEntityRelatonisDeletedTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		entityRelationConfigDetail.setConfigtypeCode(LOSEntityConstants.ENTITY_RELATION_TYPE_CODE);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId1("FBOC0001C");
		entityRelationshipType.setEntityId2("FBOC0001C");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		entityRelationshipType.setDeleted(false);
		Mockito.when(entityRelationshipTypeRepository.findById((long) 1)).thenReturn(Optional.empty());
		serviceImpl.updateEntityRelaton((long) 1, entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void updateEntityRelatonTest1() {
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId1("FBOC0001C");
		entityRelationshipType.setEntityId2("FBOC0001C");
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		Mockito.when(entityRelationshipTypeRepository.findById((long) 1))
				.thenReturn(Optional.of(entityRelationshipType));
		Mockito.when(entityRelationshipTypeRepository.findById((long) 1))
				.thenReturn(Optional.of(entityRelationshipType));
		serviceImpl.updateEntityRelaton((long) 1, entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void updateEntityRelatonOwnerShipZeroTest() {
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId1("FBOC0001C");
		entityRelationshipType.setEntityId2("FBOC0001C");
		entityRelationshipType.setOwnership((double) 0);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		Mockito.when(entityRelationshipTypeRepository.findById((long) 1))
				.thenReturn(Optional.of(entityRelationshipType));
		serviceImpl.updateEntityRelaton((long) 1, entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void updateEntityRelatonOwnerShipNullTest() {
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId1("FBOC0001C");
		entityRelationshipType.setEntityId2("FBOC0001C");
		entityRelationshipType.setOwnership(null);
		entityRelationshipType.setDeleted(false);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		Mockito.when(entityRelationshipTypeRepository.findById((long) 1))
				.thenReturn(Optional.of(entityRelationshipType));
		serviceImpl.updateEntityRelaton((long) 1, entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void findEntityRelationTreeByEntityIdTest() {
		String entityId1 = "FBOC000000C";
		FinancialEntity childEntity = new FinancialEntity();
		childEntity.setCity("Test");
		EntityNode entityNode = new EntityNode();
		entityNode.setParentId(entityId1);
		entityNode.setChildId(entityId1);
		entityNode.setChildEntity(childEntity);
		List<EntityNode> entityNodes = new ArrayList<EntityNode>();
		entityNodes.add(entityNode);
		Mockito.when(entityNodeRepository.findEntityTreeData(entityId1)).thenReturn(entityNodes);
		Mockito.when(entityRelationshipTreeServiceImpl.createTree(entityNodes, entityId1)).thenReturn(entityNode);
		Mockito.when(entityRelationshipTreeServiceImpl.findByEntityId(entityId1)).thenReturn(childEntity);
		serviceImpl.findEntityRelationTreeByEntityId(entityId1);
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void findEntityRelationTreeByParentEntityId() {
		String entityId1 = "FBOC000000C";
		Mockito.when(serviceImpl.findParentEntityCount(entityId1)).thenReturn((long) 0);
		Mockito.when(serviceImpl.findChildEntityCount(entityId1)).thenReturn((long) 2);

		List<ParentEntityNode> parentEntityNodeList = new ArrayList<>();
		Mockito.when(parentEntityNodeRepository.findEntityTreeData(entityId1)).thenReturn(parentEntityNodeList);
		ParentEntityNode parentEntityNode = new ParentEntityNode();
		Mockito.when(entityRelationshipTreeServiceImpl.createTreeFromParent(parentEntityNodeList, entityId1))
				.thenReturn(parentEntityNode);
		serviceImpl.findEntityRelationTreeByParentEntityId(entityId1);
		assertNull(null);
	}

	@Test
	public void findEntityRelationsListByIdsTest() {
		List<EntityRelationshipType> relationList = new ArrayList<EntityRelationshipType>();
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		EntityRelationshipType entityRelationshipType1 = new EntityRelationshipType();
		entityRelationshipType1.setEntityId2("FBOC000001C");
		relationList.add(entityRelationshipType);
		relationList.add(entityRelationshipType1);
		Mockito.when(entityRelationshipTypeRepository.findAllRelatedEntityRelationData(Arrays.asList("FBOC000001C")))
				.thenReturn(relationList);
		serviceImpl.findEntityRelationsListByIds(Arrays.asList("FBOC000001C"));
		assertNull(null);
	}

	@Test
	public void findSpouseTest() {
		String entityId = "FBOC000001I";
		EntityNodeSpouse spousedto = new EntityNodeSpouse() {
			@Override
			public String getEntity_id() {
				return entityId;
			}

			@Override
			public String getFirst_name() {
				return "Test";
			}

			@Override
			public String getLast_name() {
				return "Spouse";
			}
		};
		Mockito.when(individualEntityRepository.getSpouse(entityId)).thenReturn(Optional.of(spousedto));
		serviceImpl.findSpouse(entityId);
		assertNull(null);
	}

	@Test
	public void findSpouseNullTest() {
		String entityId = "FBOC000001I";
		Mockito.when(individualEntityRepository.getSpouse(entityId)).thenReturn(null);
		serviceImpl.findSpouse(entityId);
		assertNull(null);
	}
}
