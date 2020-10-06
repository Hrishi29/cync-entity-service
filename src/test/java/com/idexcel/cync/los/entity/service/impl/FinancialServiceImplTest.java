package com.idexcel.cync.los.entity.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
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
import com.idexcel.cync.los.entity.validator.FinancialServiceImplValidator;

@RunWith(SpringJUnit4ClassRunner.class)
public class FinancialServiceImplTest {

	public static final String INDIVIDUAL_ENTITY_DTO = "/test-los-entity/individual-entity-creation.json";
	public static final String COMMERCIAL_ENTITY_DTO = "/test-los-entity/commercial-entity-creation.json";

	@InjectMocks
	FinancialServiceImpl serviceImpl;

	@MockBean
	private MessageSource errorMsgs;
	@MockBean
	private FinancialRepository financialRepository;
	@MockBean
	private LosConfigRepository losConfigRepository;
	@MockBean
	private EntityTypeLookupRepository entityTypeLookupRepository;
	@MockBean
	private IndividualEntityRepository individualEntityRepository;
	@MockBean
	private CommercialEntityRepository commercialEntityRepository;
	@MockBean
	private FinancialServiceImplValidator financialServiceImplValidator;
	@MockBean
	private IndividualNameSearchRepository individualNameSearchRepository;
	@MockBean
	private CommercialNameSearchRepository commercialNameSearchRepository;
	@MockBean
	FinancialEntityListRepository financialEntityListRepository;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deactivateEntity() {
		Mockito.when(financialRepository.deactivateEntity("FBOC000001C")).thenReturn(new Integer(1));
		serviceImpl.deactivateEntity("FBOC000001C");
		assertNull(null);
	}

	@Test
	public void activateEntity() throws Exception {
		Mockito.when(financialRepository.activateEntity("FBOC000001C")).thenReturn(new Integer(1));
		serviceImpl.activateEntity("FBOC000001C");
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void findByEntityIdFailureTest() {
		Mockito.when(financialRepository.findById("TEST000")).thenReturn(Optional.empty());
		serviceImpl.findById("TEST000");
		assertNull(null);
	}

	@Test
	public void findByEntityIdSuccessTest() {
		FinancialEntity financialEntity = new FinancialEntity();
		financialEntity.setEntityId("FBOC000001C");
		Mockito.when(financialRepository.findById(financialEntity.getEntityId()))
				.thenReturn(Optional.of(financialEntity));
		assertEquals(financialEntity.getEntityId(), serviceImpl.findById("FBOC000001C").getEntityId());
	}

	@Test
	public void findAllEntityListWithConfigIdTest() {
		List<FinancialEntityList> financialEntityList = new ArrayList<>();
		LosConfigDetails losConfigDetails = new LosConfigDetails();
		losConfigDetails.setConfigId(new Long(3000001));
		FinancialEntityList financialEntity = new FinancialEntityList();
		financialEntityList.add(financialEntity);
		Mockito.when(entityTypeLookupRepository.findById(losConfigDetails.getConfigId()))
				.thenReturn(Optional.of(losConfigDetails));
		Mockito.when(financialEntityListRepository.findByEntityTypeConfig(Optional.of(losConfigDetails),
				Sort.by(Sort.Order.desc("audit.createdAt")))).thenReturn(financialEntityList);
		serviceImpl.findAllEntity(losConfigDetails);
		assertNull(null);
	}

	@Test
	public void findAllEntityListWithoutConfigIdTest() {
		List<FinancialEntityList> financialEntityList = new ArrayList<>();
		LosConfigDetails losConfigDetails = new LosConfigDetails();
		losConfigDetails.setConfigId(null);
		FinancialEntityList financialEntity = new FinancialEntityList();
		financialEntityList.add(financialEntity);
		Mockito.when(entityTypeLookupRepository.findById(losConfigDetails.getConfigId())).thenReturn(Optional.empty());
		Mockito.when(financialEntityListRepository.findAll(Sort.by(Sort.Order.desc("audit.createdAt"))))
				.thenReturn(financialEntityList);
		serviceImpl.findAllEntity(losConfigDetails);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameTest() {
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining("Test"))
				.thenReturn(commercialEntityList);
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingOrMiddleNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
						"Test", "Test", "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName("Test");
		assertNull(null);
	}

	@Test
	public void searchEntityByNameNullValueCommercialValueTest() {
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining("Test")).thenReturn(null);
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingOrMiddleNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
						"Test", "Test", "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName("Test");
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualThreeStringTest() {
		String entityName = "Test a name";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						"Test", "a", "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualThreeMNameStringTest1() {
		String entityName = "Test abc name test";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						"Test", "abc", "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualThreeMNameStringTest2() {
		String entityName = "Test a name test";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						"Test", "abc", "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualThreeMNameStringTest3() {
		String entityName = "Test  name test";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						"Test", "abc", "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualThreeStringTest1() {
		String entityName = "Test  name";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						"Test", null, "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualTwoStringTest1() {
		String entityName = "Test n";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						"Test", null, "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualLastOneStringTest() {
		String entityName = "Test";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository.findByFirstNameIgnoreCaseContaining("Test"))
				.thenReturn(new ArrayList<>());
		Mockito.when(individualEntityRepository.findByLastNameIgnoreCaseContaining("Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualLastOneStringTest1() {
		String entityName = "Test";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository.findByFirstNameIgnoreCaseContaining("Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualTwoStringTest2() {
		String entityName = "Test  ";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContaining("Test", ""))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameNull() {
		String entityName = "";
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining(entityName))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						"Test", null, "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName(entityName);
		assertNull(null);
	}

	@Test
	public void searchEntityByNameIndividualTwoStringTest() {
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialEntityRepository.findByBusinessNameIgnoreCaseContaining("Test"))
				.thenReturn(commercialEntityList);
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualEntityRepository
				.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining("Test", "name"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchEntityByName("Test name");
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void searchDuplicateEntityNameWrongConfigcodeTest() {
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000009);
		Mockito.when(losConfigRepository.findById(losConfig.getConfigId())).thenReturn(Optional.empty());
		serviceImpl.searchDuplicateEntityName(((long) 3000009), "test", null, null, null);
		assertNull(null);
	}

	@Test
	public void searchDuplicateCommercialEntityNameTest() {
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		losConfig.setConfigCode(LOSEntityConstants.COMMERCIAL_CONFIG_CODE);
		Mockito.when(losConfigRepository.findById(losConfig.getConfigId())).thenReturn(Optional.of(losConfig));
		List<CommercialEntity> commercialEntityList = new ArrayList<>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setBusinessName("Test");
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialNameSearchRepository.findByBusinessNameIgnoreCase("test"))
				.thenReturn(commercialEntityList);
		serviceImpl.searchDuplicateEntityName(((long) 3000002), "test", null, null, null);
		assertNull(null);
	}

	@Test
	public void searchDuplicateCommercialEntityNameEmptyTest() {
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		losConfig.setConfigCode(LOSEntityConstants.COMMERCIAL_CONFIG_CODE);
		Mockito.when(losConfigRepository.findById(losConfig.getConfigId())).thenReturn(Optional.of(losConfig));
		List<CommercialEntity> commercialEntityList = new ArrayList<CommercialEntity>();
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setBusinessName("Test");
		commercialEntityList.add(commercialEntity);
		Mockito.when(commercialNameSearchRepository.findByBusinessNameIgnoreCase(null))
				.thenReturn(commercialEntityList);
		serviceImpl.searchDuplicateEntityName(((long) 3000002), "Test", null, null, null);
		assertNull(null);
	}

	@Test
	public void searchDuplicateIndividualEntityNameTest() {
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		Mockito.when(losConfigRepository.findById(losConfig.getConfigId())).thenReturn(Optional.of(losConfig));
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();
		individualFinancialEntity.setFirstName("Test");
		individualFinancialEntity.setMiddleName("t");
		individualFinancialEntity.setLastName("Test");
		individualFinancialEntityList.add(individualFinancialEntity);
		Mockito.when(individualNameSearchRepository
				.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase("Test", "t", "Test"))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchDuplicateEntityName(((long) 3000001), null, "Test", "t", "Test");
		assertNull(null);
	}

	@Test
	public void searchDuplicateIndividualEntityNameEmptyTest() {
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		Mockito.when(losConfigRepository.findById(losConfig.getConfigId())).thenReturn(Optional.of(losConfig));
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		Mockito.when(individualNameSearchRepository
				.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(null, null, null))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchDuplicateEntityName(((long) 3000001), null, "Test", "t", "Test");
		assertNull(null);
	}

	@Test
	public void searchDuplicateIndividualEntityNameEmptyTest1() {
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode("TEST");
		Mockito.when(losConfigRepository.findById(losConfig.getConfigId())).thenReturn(Optional.of(losConfig));
		List<IndividualFinancialEntity> individualFinancialEntityList = new ArrayList<IndividualFinancialEntity>();
		Mockito.when(individualNameSearchRepository
				.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(null, null, null))
				.thenReturn(individualFinancialEntityList);
		serviceImpl.searchDuplicateEntityName(((long) 3000001), null, "Test", "t", "Test");
		assertNull(null);
	}

	@Test
	public void saveEntityTest() throws IOException {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("COMMERCIAL");
		entityTypeConfig.setConfigId((long) 3000002);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		commercialEntity.setBusinessName("Commercial Test");
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		Mockito.doNothing().when(financialServiceImplValidator).isValidClient(commercialEntity);
		Mockito.when(losConfigRepository.findById((long) 3000002)).thenReturn(Optional.of(losConfig));
		Mockito.doNothing().when(financialServiceImplValidator).isCountryStateNullOrNot(commercialEntity);
		Mockito.doNothing().when(financialServiceImplValidator).isEntitiyTypeConfigIdValid(commercialEntity,
				(long) 3000002);
		Mockito.doNothing().when(financialServiceImplValidator).isValidEntity(commercialEntity, losConfig);
		Mockito.doNothing().when(financialServiceImplValidator).isValidNaicsCode(commercialEntity);
		Mockito.doNothing().when(financialServiceImplValidator).isValidRM(commercialEntity);
		Mockito.when(losConfigRepository.findById(losConfig.getConfigId())).thenReturn(Optional.of(losConfig));
		Mockito.when(financialRepository.save(commercialEntity)).thenReturn(commercialEntity);
		String result = serviceImpl.saveEntity(commercialEntity, (long) 3000002);
		assertEquals(commercialEntity.getEntityId(), result);
	}

	@Test(expected = BadRequestException.class)
	public void saveEntityTest1() throws IOException {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("TEST");
		entityTypeConfig.setConfigId(null);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		commercialEntity.setBusinessName("Commercial Test");
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		Mockito.doNothing().when(financialServiceImplValidator).isValidClient(commercialEntity);
		Mockito.when(losConfigRepository.findById((long) 3000002)).thenReturn(Optional.of(losConfig));
		Mockito.when(financialRepository.save(commercialEntity)).thenReturn(commercialEntity);
		serviceImpl.saveEntity(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test
	public void saveEntityTest2() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("COMMERCIAL");
		entityTypeConfig.setConfigId(null);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		commercialEntity.setBusinessName("Commercial Test");
		commercialEntity.setEntityTypeConfig(null);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		Mockito.doNothing().when(financialServiceImplValidator).isValidClient(commercialEntity);
		Mockito.when(losConfigRepository.findById((long) 3000002)).thenReturn(Optional.of(losConfig));
		Mockito.when(financialRepository.save(commercialEntity)).thenReturn(commercialEntity);
		serviceImpl.saveEntity(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void saveEntityLosConfigFailureTest() throws IOException {
		CommercialEntity commercialEntity = new CommercialEntity();
		Mockito.doNothing().when(financialServiceImplValidator).isValidClient(commercialEntity);
		Mockito.when(losConfigRepository.findById((long) 3000002)).thenReturn(Optional.empty());
		serviceImpl.saveEntity(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void saveEntityLosConfigTypeIdFailureTest() throws IOException {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(((long) 3000003));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		Mockito.doNothing().when(financialServiceImplValidator).isValidClient(commercialEntity);
		Mockito.when(losConfigRepository.findById((long) 3000002)).thenReturn(Optional.of(losConfig));
		serviceImpl.saveEntity(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test
	public void updateEntityTest() throws IOException {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		commercialEntity.setBusinessName("Commercial Test");
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		Mockito.doNothing().when(financialServiceImplValidator).isValidClient(commercialEntity);
		FinancialEntity financialEntity = new FinancialEntity();
		financialEntity.setEntityId("FBOC000001C");
		Mockito.when(financialRepository.findById("FBOC000001C")).thenReturn(Optional.of(financialEntity));
		Mockito.when(losConfigRepository.findById((long) 3000002)).thenReturn(Optional.of(losConfig));
		Mockito.doNothing().when(financialServiceImplValidator).isCountryStateNullOrNot(commercialEntity);
		Mockito.doNothing().when(financialServiceImplValidator).isEntitiyTypeConfigIdValid(commercialEntity,
				(long) 3000002);
		Mockito.doNothing().when(financialServiceImplValidator).isValidEntity(commercialEntity, losConfig);
		Mockito.doNothing().when(financialServiceImplValidator).isValidNaicsCode(commercialEntity);
		Mockito.doNothing().when(financialServiceImplValidator).isValidRM(commercialEntity);
		Mockito.when(losConfigRepository.findById(losConfig.getConfigId())).thenReturn(Optional.of(losConfig));
		Mockito.when(financialRepository.save(commercialEntity)).thenReturn(commercialEntity);
		String result = serviceImpl.updateEntity((long) 3000002, "FBOC000001C", commercialEntity);
		assertEquals(commercialEntity.getEntityId(), result);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void updateEntityIdNotFoundTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		commercialEntity.setBusinessName("Commercial Test");
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		Mockito.doNothing().when(financialServiceImplValidator).isValidClient(commercialEntity);
		Mockito.when(financialRepository.findById("FBOC000001C")).thenReturn(Optional.empty());
		String result = serviceImpl.updateEntity((long) 3000002, "FBOC000001C", commercialEntity);
		assertEquals(commercialEntity.getEntityId(), result);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void updateEntityLosConfigFailureTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		FinancialEntity financialEntity = new FinancialEntity();
		financialEntity.setEntityId("FBOC000001C");
		Mockito.when(financialRepository.findById("FBOC000001C")).thenReturn(Optional.of(financialEntity));
		Mockito.doNothing().when(financialServiceImplValidator).isValidClient(commercialEntity);
		Mockito.when(losConfigRepository.findById((long) 3000009)).thenReturn(Optional.empty());
		serviceImpl.updateEntity((long) 3000002, "FBOC000001C", commercialEntity);
		assertNull(null);
	}
}