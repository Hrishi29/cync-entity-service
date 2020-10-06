package com.idexcel.cync.los.entity.validator;

import static org.junit.Assert.assertNull;

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
import com.idexcel.cync.los.entity.dao.ChildEntityNodeRepository;
import com.idexcel.cync.los.entity.dao.ConfigLookupRepository;
import com.idexcel.cync.los.entity.dao.EntityRelationshipTypeRepository;
import com.idexcel.cync.los.entity.dao.FinancialRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.service.impl.EntityRelationshipTreeServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
public class EntityRelationshipValidatorServiceTest {

	@InjectMocks
	public EntityRelationshipValidatorService validator;

	@MockBean
	MessageSource errorMsgs;
	@MockBean
	private FinancialRepository financialRepository;
	@MockBean
	private ConfigLookupRepository configLookupRepository;
	@MockBean
	private EntityRelationshipTypeRepository entityRelationshipTypeRepository;
	@MockBean
	private ChildEntityNodeRepository childEntityNodeRepository;
	@MockBean
	private EntityRelationshipTreeServiceImpl entityRelationshipTreeServiceImpl;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		this.validator = new EntityRelationshipValidatorService(financialRepository, entityRelationshipTypeRepository,
				childEntityNodeRepository, configLookupRepository, entityRelationshipTreeServiceImpl, errorMsgs);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC00000C");
		Mockito.when(financialRepository.findById("FBOC00000C")).thenReturn(Optional.of(commercialEntity));
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2()))
				.thenReturn(Optional.of(commercialEntity));
		validator.isValidEntityRelation(entityRelationshipType, "FBOC00000C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationE1E2NotEqualTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		CommercialEntity commercialEntity2 = new CommercialEntity();
		commercialEntity2.setEntityId("FBOC000002C");
		Mockito.when(financialRepository.findById("FBOC000001C")).thenReturn(Optional.of(commercialEntity2));
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2()))
				.thenReturn(Optional.of(commercialEntity));
		validator.isValidEntityRelation(entityRelationshipType, "FBOC000001C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationEntityRelationConfigDetailNullTest() {
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(null);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		CommercialEntity commercialEntity2 = new CommercialEntity();
		commercialEntity2.setEntityId("FBOC000002C");
		Mockito.when(financialRepository.findById("FBOC000001C")).thenReturn(Optional.of(commercialEntity2));
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2()))
				.thenReturn(Optional.of(commercialEntity));
		validator.isValidEntityRelation(entityRelationshipType, "FBOC000001C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationEntityRelationConfigIdNullTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId(null);
		entityRelationConfigDetail.setConfigCode("TEST_CODE");
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		CommercialEntity commercialEntity2 = new CommercialEntity();
		commercialEntity2.setEntityId("FBOC000002C");
		Mockito.when(financialRepository.findById("FBOC000001C")).thenReturn(Optional.of(commercialEntity2));
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2()))
				.thenReturn(Optional.of(commercialEntity));
		validator.isValidEntityRelation(entityRelationshipType, "FBOC000001C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationEntityRelationOwnerShipNullTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		entityRelationConfigDetail.setConfigtypeCode(LOSEntityConstants.ENTITY_RELATION_TYPE_CODE);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(null);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		CommercialEntity commercialEntity2 = new CommercialEntity();
		commercialEntity2.setEntityId("FBOC000002C");
		Mockito.when(financialRepository.findById("FBOC000001C")).thenReturn(Optional.of(commercialEntity2));
		Mockito.when(
				configLookupRepository.findById(entityRelationshipType.getEntityRelationConfigDetail().getConfigId()))
				.thenReturn(Optional.of(entityRelationConfigDetail));
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2()))
				.thenReturn(Optional.of(commercialEntity));
		validator.isValidEntityRelation(entityRelationshipType, "FBOC000001C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationEntityRelationOwnerShipNotNullTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		entityRelationConfigDetail.setConfigtypeCode(LOSEntityConstants.ENTITY_RELATION_TYPE_CODE);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC000001C");
		entityRelationshipType.setOwnership((double) 50);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC000001C");
		Mockito.when(financialRepository.findById("FBOC000001C")).thenReturn(Optional.of(commercialEntity));
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2()))
				.thenReturn(Optional.of(commercialEntity));
		Mockito.when(
				configLookupRepository.findById(entityRelationshipType.getEntityRelationConfigDetail().getConfigId()))
				.thenReturn(Optional.of(entityRelationConfigDetail));
		validator.isValidEntityRelation(entityRelationshipType, "FBOC000001C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationEntityId2NullTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(financialRepository.findById("FBOC00000C")).thenReturn(Optional.empty());
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2())).thenReturn(Optional.empty());
		validator.isValidEntityRelation(entityRelationshipType, "FBOC00000C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationEntityId2Null1Test() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC00000C");
		Mockito.when(financialRepository.findById("FBOC00000C")).thenReturn(Optional.of(commercialEntity));
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2())).thenReturn(Optional.empty());
		validator.isValidEntityRelation(entityRelationshipType, "FBOC00000C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityRelationEntityId2Null2Test() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityId("FBOC00000C");
		Mockito.when(financialRepository.findById("FBOC00000C")).thenReturn(Optional.of(commercialEntity));
		Mockito.when(financialRepository.findById(entityRelationshipType.getEntityId2()))
				.thenReturn(Optional.of(commercialEntity));
		validator.isValidEntityRelation(entityRelationshipType, "FBOC00000C");
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityReverseRelationCombinationTest() {
		String entityId1 = "FBOC00000C";
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1AndEntityId2AndDeleted(entityId1,
				entityRelationshipType.getEntityId2(), false)).thenReturn(entityRelationshipType);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId2AndEntityId1AndDeleted(entityId1,
				entityRelationshipType.getEntityId2(), false)).thenReturn(entityRelationshipType);
		validator.validateEntityReverseRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test
	public void validateEntityReverseRelationCombinationNullTest() {
		String entityId1 = "FBOC00000C";
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setStatus(false);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1AndEntityId2AndDeleted(entityId1,
				entityRelationshipType.getEntityId2(), false)).thenReturn(null);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId2AndEntityId1AndDeleted(entityId1,
				entityRelationshipType.getEntityId2(), false)).thenReturn(null);
		validator.validateEntityReverseRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test
	public void validateEntityReverseRelationCombinationTest1() {
		String entityId1 = "FBOC00000C";
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipTypeE1E2 = new EntityRelationshipType();
		entityRelationshipTypeE1E2.setEntityId2("FBOC00000C");
		entityRelationshipTypeE1E2.setOwnership(new Double(0));
		entityRelationshipTypeE1E2.setEntityRelationConfigDetail(entityRelationConfigDetail);

		EntityRelationshipType entityRelationshipTypeE2E1 = new EntityRelationshipType();
		entityRelationshipTypeE1E2.setEntityId2("FBOC000000C");
		entityRelationshipTypeE1E2.setOwnership(new Double(0));
		entityRelationshipTypeE1E2.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1AndEntityId2AndDeleted(entityId1,
				entityRelationshipTypeE1E2.getEntityId2(), false)).thenReturn(null);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId2AndEntityId1AndDeleted(entityId1,
				entityRelationshipTypeE2E1.getEntityId2(), false)).thenReturn(entityRelationshipTypeE2E1);
		validator.validateEntityReverseRelationCombination("FBOC00000C", entityRelationshipTypeE1E2);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityReverseRelationCombinationTest12() {
		String entityId1 = "FBOC00000C";
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipTypeE1E2 = new EntityRelationshipType();
		entityRelationshipTypeE1E2.setEntityId2("FBOC00000C");
		entityRelationshipTypeE1E2.setOwnership(new Double(0));
		entityRelationshipTypeE1E2.setStatus(true);
		entityRelationshipTypeE1E2.setEntityRelationConfigDetail(entityRelationConfigDetail);

		EntityRelationshipType entityRelationshipTypeE2E1 = new EntityRelationshipType();
		entityRelationshipTypeE2E1.setEntityId2("FBOC000000C");
		entityRelationshipTypeE2E1.setOwnership(new Double(0));
		entityRelationshipTypeE2E1.setStatus(true);
		entityRelationshipTypeE2E1.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1AndEntityId2AndDeleted(entityId1,
				entityRelationshipTypeE1E2.getEntityId2(), false)).thenReturn(entityRelationshipTypeE1E2);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId2AndEntityId1AndDeleted(entityId1,
				entityRelationshipTypeE2E1.getEntityId2(), false)).thenReturn(null);
		validator.validateEntityReverseRelationCombination("FBOC00000C", entityRelationshipTypeE1E2);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityReverseRelationCombinationTest3() {
		String entityId1 = "FBOC00000C";
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipTypeE1E2 = new EntityRelationshipType();
		entityRelationshipTypeE1E2.setEntityId2("FBOC00000C");
		entityRelationshipTypeE1E2.setOwnership(new Double(0));
		entityRelationshipTypeE1E2.setStatus(true);
		entityRelationshipTypeE1E2.setEntityRelationConfigDetail(entityRelationConfigDetail);

		EntityRelationshipType entityRelationshipTypeE2E1 = new EntityRelationshipType();
		entityRelationshipTypeE2E1.setEntityId2("FBOC000000C");
		entityRelationshipTypeE2E1.setOwnership(new Double(0));
		entityRelationshipTypeE2E1.setStatus(true);
		entityRelationshipTypeE2E1.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1AndEntityId2AndDeleted(entityId1,
				entityRelationshipTypeE1E2.getEntityId2(), false)).thenReturn(entityRelationshipTypeE1E2);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId2AndEntityId1AndDeleted(entityId1,
				entityRelationshipTypeE2E1.getEntityId2(), false)).thenReturn(entityRelationshipTypeE2E1);
		validator.validateEntityReverseRelationCombination("FBOC00000C", entityRelationshipTypeE1E2);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityReverseRelationCombinationTest4() {
		String entityId1 = "FBOC00000C";
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipTypeE1E2 = new EntityRelationshipType();
		entityRelationshipTypeE1E2.setEntityId2("FBOC00000C");
		entityRelationshipTypeE1E2.setOwnership(new Double(0));
		entityRelationshipTypeE1E2.setStatus(false);
		entityRelationshipTypeE1E2.setEntityRelationConfigDetail(entityRelationConfigDetail);

		EntityRelationshipType entityRelationshipTypeE2E1 = new EntityRelationshipType();
		entityRelationshipTypeE2E1.setEntityId2("FBOC000000C");
		entityRelationshipTypeE2E1.setOwnership(new Double(0));
		entityRelationshipTypeE2E1.setStatus(true);
		entityRelationshipTypeE2E1.setEntityRelationConfigDetail(entityRelationConfigDetail);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1AndEntityId2AndDeleted(entityId1,
				entityRelationshipTypeE1E2.getEntityId2(), false)).thenReturn(entityRelationshipTypeE1E2);
		Mockito.when(entityRelationshipTypeRepository.findByEntityId2AndEntityId1AndDeleted(entityId1,
				entityRelationshipTypeE2E1.getEntityId2(), false)).thenReturn(entityRelationshipTypeE2E1);
		validator.validateEntityReverseRelationCombination("FBOC00000C", entityRelationshipTypeE1E2);
		assertNull(null);
	}

	@Test
	public void validateEntityRelationCombinationAffiliatedTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationNotAffiliatedTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000001);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test
	public void validateEntityRelationCombinationSubsidiaryTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000009);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationAffiliatedTest1() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationSubsidiaryTest1() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000009);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}

	@Test
	public void validateEntityRelationCombinationOwnerTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000008);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test
	public void validateEntityRelationCombinationOwnerResverseTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000008);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}
	
	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationOwnerTest1() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000011);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setOwnership(new Double(90));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}
	
	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationOwnerOwnershipTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000008);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(null);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}
	
	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationOwnerOwnershipTest2() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000008);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(190));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}
	
	@Test
	public void validateEntityRelationCombinationAffiliatedResverseTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000007);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00001C", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationOwnerNullTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000008);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationOwnedTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000011);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000C", entityRelationshipType);
		assertNull(null);
	}

	@Test
	public void validateEntityRelationCombinationOwnedResverseTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000011);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(0));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationSpouseNegativeOwnershipTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000011);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setOwnership(new Double(-10));
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}
	
	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationSpouseTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000010);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		entityRelationshipType.setOwnership(new Double(10));
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationSpouseTest1() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000010);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		entityRelationshipType.setOwnership(null);
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationSpouseNoOwnerShipTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000010);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000I");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationSpouseWrongEntityIdTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000010);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		entityRelationshipType.setOwnership(new Double(0));
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationSpouseWrongConfigIdTest() {
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId((long) 3000000);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setEntityId2("FBOC00000C");
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		entityRelationshipType.setOwnership(new Double(0));
		validator.validateEntityRelationCombination("FBOC00000I", entityRelationshipType);
		assertNull(null);
	}

	@Test
	public void validateEntityRelationCombinationsOwnership() {
		validator.validateEntityRelationCombinationsOwnership(new Double(0));
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationsOwnershipNegativeValue() {
		validator.validateEntityRelationCombinationsOwnership(new Double(-10));
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void validateEntityRelationCombinationsOwnershipPositoveValue() {
		validator.validateEntityRelationCombinationsOwnership(new Double(10));
		assertNull(null);
	}
	
	@Test
	public void isValidSpouseCountTest(){
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType() ;
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId(LOSEntityConstants.SPOUSE);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		String entityId1= "FBOC000001I";
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1SpouseCount(entityId1,
				LOSEntityConstants.SPOUSE)).thenReturn(0);
		Mockito.when(entityRelationshipTypeRepository
		.findByEntityId2SpouseCount(entityRelationshipType.getEntityId2(), LOSEntityConstants.SPOUSE)).thenReturn(0);
		validator.isValidSpouseCount(entityRelationshipType,entityId1);
		assertNull(null);
	}
	@Test(expected = BadRequestException.class)
	public void isValidSpouseCountGreater1Test(){
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType() ;
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId(LOSEntityConstants.SPOUSE);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		String entityId1= "FBOC000001I";
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1SpouseCount(entityId1,
				LOSEntityConstants.SPOUSE)).thenReturn(1);
		Mockito.when(entityRelationshipTypeRepository
		.findByEntityId2SpouseCount(entityRelationshipType.getEntityId2(), LOSEntityConstants.SPOUSE)).thenReturn(0);
		validator.isValidSpouseCount(entityRelationshipType,entityId1);
		assertNull(null);
	}
	@Test(expected = BadRequestException.class)
	public void isValidSpouseCountGreater2Test(){
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType() ;
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId(LOSEntityConstants.SPOUSE);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		String entityId1= "FBOC000001I";
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1SpouseCount(entityId1,
				LOSEntityConstants.SPOUSE)).thenReturn(0);
		Mockito.when(entityRelationshipTypeRepository
		.findByEntityId2SpouseCount(entityRelationshipType.getEntityId2(), LOSEntityConstants.SPOUSE)).thenReturn(1);
		validator.isValidSpouseCount(entityRelationshipType,entityId1);
		assertNull(null);
	}
	
	@Test
	public void isValidSpouseCountInvalidEntityIdTest(){
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType() ;
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId(LOSEntityConstants.SPOUSE);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		String entityId1= "FBOC000001C";
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1SpouseCount(entityId1,
				LOSEntityConstants.SPOUSE)).thenReturn(0);
		Mockito.when(entityRelationshipTypeRepository
		.findByEntityId2SpouseCount(entityRelationshipType.getEntityId2(), LOSEntityConstants.SPOUSE)).thenReturn(1);
		validator.isValidSpouseCount(entityRelationshipType,entityId1);
		assertNull(null);
	}
	
	@Test
	public void isValidSpouseCountInvalidRelationIdTest(){
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType() ;
		LosConfigDetails entityRelationConfigDetail = new LosConfigDetails();
		entityRelationConfigDetail.setConfigId(LOSEntityConstants.OWNER);
		entityRelationshipType.setEntityRelationConfigDetail(entityRelationConfigDetail);
		String entityId1= "FBOC000001I";
		Mockito.when(entityRelationshipTypeRepository.findByEntityId1SpouseCount(entityId1,
				LOSEntityConstants.SPOUSE)).thenReturn(0);
		Mockito.when(entityRelationshipTypeRepository
		.findByEntityId2SpouseCount(entityRelationshipType.getEntityId2(), LOSEntityConstants.SPOUSE)).thenReturn(1);
		validator.isValidSpouseCount(entityRelationshipType,entityId1);
		assertNull(null);
	}
}
