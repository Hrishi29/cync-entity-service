package com.idexcel.cync.los.entity.validator;

import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.codec.Charsets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.ExternalAPICaller;
import com.idexcel.cync.los.entity.dao.ConfigLookupRepository;
import com.idexcel.cync.los.entity.dao.FinancialRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.model.Audit;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfig;
import com.idexcel.cync.los.entity.model.LosConfigDetails;

@RunWith(SpringJUnit4ClassRunner.class)
public class FinancialServiceImplValidatorTest {

	public static final String ADMIN_RESPONSE = "/test-los-entity/admin-response.json";
	public static final String ADMIN_RESPONSE2 = "/test-los-entity/admin-response2.json";
	public static final String ADMIN_RESPONSE3 = "/test-los-entity/admin-response3.json";
	public static final String ADMIN_RESPONSE4 = "/test-los-entity/admin-response4.json";

	@InjectMocks
	public FinancialServiceImplValidator validator;

	@MockBean
	private FinancialRepository financialRepository;

	@MockBean
	private ConfigLookupRepository configLookupRepository;

	@MockBean
	private ClientValidator clientValidator;

	@MockBean
	private FSCountryStateValidator fSCountryStateValidator;

	@MockBean
	private ExternalAPICaller externalAPICaller;

	@MockBean
	MessageSource errorMsgs;

	@MockBean
	RestTemplate restTemplate;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		this.validator = new FinancialServiceImplValidator(financialRepository, configLookupRepository, clientValidator,
				fSCountryStateValidator, errorMsgs);
	}

	@Test
	public void isValidEntityCommercialEntityTest() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("COMMERCIAL");
		entityTypeConfig.setConfigId(((long) 3000002));
		entityTypeConfig.setConfigtypeCode("ENTITY");
		entityTypeConfig.setConfigtypeId((long) 2000003);
		entityTypeConfig.setConfigValue("Commercial");
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigCode("LLC");
		businessType.setConfigId((long) 3000003);
		businessType.setConfigtypeCode("BUSINESS_TYPE");
		businessType.setConfigtypeId((long) 2000004);
		businessType.setConfigValue("LLC");
		CommercialEntity entity = new CommercialEntity();
		entity.setBusinessType(businessType);
		entity.setEntityTypeConfig(entityTypeConfig);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		losConfig.setConfigCode(LOSEntityConstants.COMMERCIAL_CONFIG_CODE);
		Mockito.when(configLookupRepository.findById(businessType.getConfigId())).thenReturn(Optional.of(businessType));
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@Test
	public void isValidEntityIndividualFinancialEntityTest() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("INDIVIDUAL");
		entityTypeConfig.setConfigId(((long) 3000001));
		entityTypeConfig.setConfigtypeCode("ENTITY");
		entityTypeConfig.setConfigtypeId((long) 2000003);
		entityTypeConfig.setConfigValue("Individual");
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigCode("LLC");
		businessType.setConfigId((long) 3000003);
		businessType.setConfigtypeCode("BUSINESS_TYPE");
		businessType.setConfigtypeId((long) 2000004);
		businessType.setConfigValue("LLC");
		IndividualFinancialEntity entity = new IndividualFinancialEntity();
		entity.setDateOfBirth(null);
		entity.setEntityTypeConfig(entityTypeConfig);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		Mockito.when(configLookupRepository.findById(businessType.getConfigId())).thenReturn(Optional.of(businessType));
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityIndividualFinancialEntityWrongConfigCodeTest() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("INDIVIDUAL");
		entityTypeConfig.setConfigId(((long) 3000001));
		entityTypeConfig.setConfigtypeCode("ENTITY");
		entityTypeConfig.setConfigtypeId((long) 2000003);
		entityTypeConfig.setConfigValue("Individual");
		IndividualFinancialEntity entity = new IndividualFinancialEntity();
		entity.setDateOfBirth(null);
		entity.setEntityTypeConfig(entityTypeConfig);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.COMMERCIAL_CONFIG_CODE);
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = BadRequestException.class)
	public void isValidEntityIndividualFinancialEntityDOBTest() {
		Date date = new Date();
		date.setDate(32);// NOSONAR
		date.setMonth(02);// NOSONAR
		date.setYear(2019);// NOSONAR
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("INDIVIDUAL");
		entityTypeConfig.setConfigId(((long) 3000001));
		entityTypeConfig.setConfigtypeCode("ENTITY");
		entityTypeConfig.setConfigtypeId((long) 2000003);
		entityTypeConfig.setConfigValue("Individual");
		IndividualFinancialEntity entity = new IndividualFinancialEntity();
		entity.setEntityTypeConfig(entityTypeConfig);
		entity.setDateOfBirth(date);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void isValidEntityIndividualFinancialEntityDOBTest1() {
		Date date = new Date();
		date.setDate(0);// NOSONAR
		date.setMonth(0);// NOSONAR
		date.setYear(0);// NOSONAR
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("INDIVIDUAL");
		entityTypeConfig.setConfigId(((long) 3000001));
		entityTypeConfig.setConfigtypeCode("ENTITY");
		entityTypeConfig.setConfigtypeId((long) 2000003);
		entityTypeConfig.setConfigValue("Individual");
		IndividualFinancialEntity entity = new IndividualFinancialEntity();
		entity.setEntityTypeConfig(entityTypeConfig);
		entity.setDateOfBirth(date);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@Test
	public void isValidEntityIndividualFinancialEntityDOBNullTest() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigCode("INDIVIDUAL");
		entityTypeConfig.setConfigId(((long) 3000001));
		entityTypeConfig.setConfigtypeCode("ENTITY");
		entityTypeConfig.setConfigtypeId((long) 2000003);
		entityTypeConfig.setConfigValue("Individual");
		IndividualFinancialEntity entity = new IndividualFinancialEntity();
		entity.setDateOfBirth(null);
		entity.setEntityTypeConfig(entityTypeConfig);
		entity.setDateOfBirth(null);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000001);
		losConfig.setConfigCode(LOSEntityConstants.INDIVIDUAL_CONFIG_CODE);
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidEntityInvalidEntityTypeIdTest() {
		CommercialEntity entity = new CommercialEntity();
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		losConfig.setConfigCode("TEST");
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = BadRequestException.class)
	public void isValidEntityBusinessOpenDateTest() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigCode("LLC");
		businessType.setConfigId((long) 3000003);
		businessType.setConfigtypeCode("BUSINESS_TYPE");
		businessType.setConfigtypeId((long) 2000004);
		businessType.setConfigValue("LLC");
		Date date = new Date();
		date.setDate(32);// NOSONAR
		date.setMonth(02);// NOSONAR
		date.setYear(2019);// NOSONAR
		CommercialEntity entity = new CommercialEntity();
		entity.setBusinessOpenDate(date);
		entity.setBusinessType(businessType);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		losConfig.setConfigCode(LOSEntityConstants.COMMERCIAL_CONFIG_CODE);
		Mockito.when(configLookupRepository.findById(businessType.getConfigId())).thenReturn(Optional.of(businessType));
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void isValidEntityBusinessOpenDateTest2() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigCode("LLC");
		businessType.setConfigId((long) 3000003);
		businessType.setConfigtypeCode("BUSINESS_TYPE");
		businessType.setConfigtypeId((long) 2000004);
		businessType.setConfigValue("LLC");
		Date date = new Date();
		date.setDate(0);// NOSONAR
		date.setMonth(0);// NOSONAR
		date.setYear(0);// NOSONAR
		CommercialEntity entity = new CommercialEntity();
		entity.setBusinessOpenDate(date);
		entity.setBusinessType(businessType);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		losConfig.setConfigCode(LOSEntityConstants.COMMERCIAL_CONFIG_CODE);
		Mockito.when(configLookupRepository.findById(businessType.getConfigId())).thenReturn(Optional.of(businessType));
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@Test
	public void isValidEntityBusinessOpenDateTest1() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigCode("LLC");
		businessType.setConfigId((long) 3000003);
		businessType.setConfigtypeCode("BUSINESS_TYPE");
		businessType.setConfigtypeId((long) 2000004);
		businessType.setConfigValue("LLC");
		Date date = null;
		CommercialEntity entity = new CommercialEntity();
		entity.setBusinessOpenDate(date);
		entity.setBusinessType(businessType);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		losConfig.setConfigCode(LOSEntityConstants.COMMERCIAL_CONFIG_CODE);
		Mockito.when(configLookupRepository.findById(businessType.getConfigId())).thenReturn(Optional.of(businessType));
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@Test
	public void isValidEntityBusinessOpenDateNullTest() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigCode("LLC");
		businessType.setConfigId((long) 3000003);
		businessType.setConfigtypeCode("BUSINESS_TYPE");
		businessType.setConfigtypeId((long) 2000004);
		businessType.setConfigValue("LLC");
		CommercialEntity entity = new CommercialEntity();
		entity.setBusinessOpenDate(null);
		entity.setBusinessType(businessType);
		LosConfig losConfig = new LosConfig();
		losConfig.setConfigId((long) 3000002);
		losConfig.setConfigCode(LOSEntityConstants.COMMERCIAL_CONFIG_CODE);
		Mockito.when(configLookupRepository.findById(businessType.getConfigId())).thenReturn(Optional.of(businessType));
		validator.isValidEntity(entity, losConfig);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidNaicsCodeTest() {
		LosConfigDetails naics = new LosConfigDetails();
		naics.setConfigCode("111");
		naics.setConfigId((long) 6000001);
		naics.setConfigtypeCode(LOSEntityConstants.NAICS_CODE);
		naics.setConfigtypeId((long) 2000006);
		naics.setConfigValue("Crop Production");
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setNaics(naics);
		validator.isValidNaicsCode(commercialEntity);
		assertNull(null);
	}

	@Test
	public void isValidNaicsCodeNullTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setNaics(null);
		validator.isValidNaicsCode(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidNaicsCodeWrongConfigtypeCodeTest() {
		LosConfigDetails naics = new LosConfigDetails();
		naics.setConfigCode("111");
		naics.setConfigId((long) 6000001);
		naics.setConfigtypeCode("TEST_CODE");
		naics.setConfigtypeId((long) 2000006);
		naics.setConfigValue("Crop Production");
		Mockito.when(configLookupRepository.findById(naics.getConfigId())).thenReturn(Optional.of(naics));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setNaics(naics);
		validator.isValidNaicsCode(commercialEntity);
		assertNull(null);
	}

	@Test
	public void isValidNaicsCodeWrongInstanceTest() {
		FinancialEntity financialEntity = new FinancialEntity();
		validator.isValidNaicsCode(financialEntity);
		assertNull(null);
	}

	@Test
	public void isValidNaicsCodeNullNaicsCodeIdTest() {
		LosConfigDetails naics = new LosConfigDetails();
		naics.setConfigCode("111");
		naics.setConfigId(null);
		naics.setConfigtypeCode(LOSEntityConstants.NAICS_CODE);
		naics.setConfigtypeId((long) 2000006);
		naics.setConfigValue("Crop Production");
		Mockito.when(configLookupRepository.findById((long) 600000)).thenReturn(Optional.of(naics));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setNaics(naics);
		validator.isValidNaicsCode(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidNaicsCodeWrongNaicsCodeIdWithNullTest() {
		LosConfigDetails naics = new LosConfigDetails();
		naics.setConfigId((long) 123450);
		Mockito.when(configLookupRepository.findById((long) 123450)).thenReturn(Optional.empty());
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setNaics(naics);
		validator.isValidNaicsCode(commercialEntity);
		assertNull(null);
	}

	@Test
	public void isValidNaicsCodeWrongNaicsCodeIdTest() {
		LosConfigDetails naics = new LosConfigDetails();
		naics.setConfigId((long) 1220000);
		naics.setConfigtypeCode(LOSEntityConstants.NAICS_CODE);
		Mockito.when(configLookupRepository.findById(naics.getConfigId())).thenReturn(Optional.of(naics));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setNaics(naics);
		validator.isValidNaicsCode(commercialEntity);
		assertNull(null);
	}

	@Test
	public void isValidClientTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		Mockito.doNothing().when(clientValidator).isValidClient(commercialEntity);
		validator.isValidClient(commercialEntity);
		assertNull(null);
	}

	@Test
	public void isCountryStateNullOrNotTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		Mockito.doNothing().when(fSCountryStateValidator).isCountryStateNullOrNot(commercialEntity);
		validator.isCountryStateNullOrNot(commercialEntity);
		assertNull(null);
	}

	@Test
	public void isValidRMNullTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setRelationshipManagerEmail(null);
		validator.isValidRM(commercialEntity);
		assertNull(null);
	}

	@Test
	public void isValidRMEmptyTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setRelationshipManagerEmail("");
		validator.isValidRM(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public <T> void isValidRMTest() throws Exception {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setRelationshipManagerEmail("Test@x.com");
		Mockito.when(externalAPICaller.post(CoreConstants.ADMIN_POST_USER_URL, Arrays.asList("Test@x.com")))
				.thenReturn(new ResponseEntity<Object>(ResponseEntity.ok(new ObjectMapper().readValue(Resources
						.toString(Resources.getResource(FinancialServiceImplValidatorTest.class, ADMIN_RESPONSE),
								Charsets.UTF_8),
						Object.class)), HttpStatus.OK));
		validator.isValidRM(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public <T> void isValidRMTest1() throws Exception {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setRelationshipManagerEmail("Test@x.com");
		Mockito.when(externalAPICaller.post(CoreConstants.ADMIN_POST_USER_URL, Arrays.asList("Test@x.com")))
				.thenReturn(new ResponseEntity<Object>(ResponseEntity.ok(new ObjectMapper().readValue(Resources
						.toString(Resources.getResource(FinancialServiceImplValidatorTest.class, ADMIN_RESPONSE),
								Charsets.UTF_8),
						Object.class)), HttpStatus.OK));
		validator.isValidRM(commercialEntity);
		assertNull(null);
	}

	@Test
	public <T> void isValidRMTest2() throws Exception {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setRelationshipManagerEmail(null);
		Mockito.when(externalAPICaller.post(CoreConstants.ADMIN_POST_USER_URL, Arrays.asList("Test@x.com")))
				.thenReturn(null);
		validator.isValidRM(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidRoleForRelationshipManagerisBaseTrueTest() throws Exception {
		validator.isValidRoleForRelationshipManager(ResponseEntity.ok().body(new ObjectMapper().readValue(Resources.toString(
				Resources.getResource(FinancialServiceImplValidatorTest.class, ADMIN_RESPONSE), Charsets.UTF_8),
				Object.class)));
		assertNull(null);
	}
	@Test(expected = BadRequestException.class)
	public void isValidRoleForRelationshipManagerisBaseFalseTest() throws Exception {
		validator.isValidRoleForRelationshipManager(ResponseEntity.ok().body(new ObjectMapper().readValue(Resources.toString(
				Resources.getResource(FinancialServiceImplValidatorTest.class, ADMIN_RESPONSE2), Charsets.UTF_8),
				Object.class)));
		assertNull(null);
	}
	@Test(expected = BadRequestException.class)
	public void isValidRoleForRelationshipManagerisBaseFalseCustomTest() throws Exception {
		validator.isValidRoleForRelationshipManager(ResponseEntity.ok().body(new ObjectMapper().readValue(Resources.toString(
				Resources.getResource(FinancialServiceImplValidatorTest.class, ADMIN_RESPONSE3), Charsets.UTF_8),
				Object.class)));
		assertNull(null);
	}
	@Test
	public void isValidRoleForRelationshipManagerisBaseTrueCustomTest1() throws Exception {
		validator.isValidRoleForRelationshipManager(ResponseEntity.ok().body(new ObjectMapper().readValue(Resources.toString(
				Resources.getResource(FinancialServiceImplValidatorTest.class, ADMIN_RESPONSE4), Charsets.UTF_8),
				Object.class)));
		assertNull(null);
	}

	@Test()
	public void isEntitiyTypeConfigIdValidtest() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(((long) 3000002));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		validator.isEntitiyTypeConfigIdValid(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isEntitiyTypeConfigIdInvalidValidtest1() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(null);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		validator.isEntitiyTypeConfigIdValid(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isEntitiyTypeConfigIdInvalidValidtest2() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(((long) 3000002));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		validator.isEntitiyTypeConfigIdValid(commercialEntity, null);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isEntitiyTypeConfigIdInvalidValidtest3() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(null);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		validator.isEntitiyTypeConfigIdValid(commercialEntity, null);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isEntitiyTypeConfigIdInvalidValidtest4() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(((long) 3000002));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		validator.isEntitiyTypeConfigIdValid(commercialEntity, (long) 30000);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isEntitiyTypeConfigIdInvalidValidtest5() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(((long) 30000));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		validator.isEntitiyTypeConfigIdValid(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isEntitiyTypeConfigIdInvalidValidtes6() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(null);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(entityTypeConfig);
		validator.isEntitiyTypeConfigIdValid(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test
	public void isEntitiyTypeConfigIdInvalidValidtest7() {
		LosConfigDetails entityTypeConfig = new LosConfigDetails();
		entityTypeConfig.setConfigId(((long) 3000002));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setEntityTypeConfig(null);
		validator.isEntitiyTypeConfigIdValid(commercialEntity, (long) 3000002);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidCreatedAtNullTest() {
		Audit audit = new Audit();
		audit.setCreatedAt(LocalDateTime.now());
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setAudit(audit);
		Audit auditDb = new Audit();
		auditDb.setCreatedAt(null);
		CommercialEntity commercialEntityDb = new CommercialEntity();
		commercialEntityDb.setAudit(auditDb);
		Mockito.when(financialRepository.findById("FBOC0000123C")).thenReturn(Optional.of(commercialEntityDb));
		validator.isValidCreatedAt(commercialEntity, "FBOC0000123C");
		assertNull(null);
	}

	@Test
	public void isValidCreatedAtNullTest1() {
		Audit audit = new Audit();
		audit.setCreatedAt(null);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setAudit(null);
		Audit auditDb = new Audit();
		auditDb.setCreatedAt(LocalDateTime.now());
		CommercialEntity commercialEntityDb = new CommercialEntity();
		commercialEntityDb.setAudit(auditDb);
		Mockito.when(financialRepository.findById("FBOC0000123C")).thenReturn(Optional.of(commercialEntityDb));
		validator.isValidCreatedAt(commercialEntity, "FBOC0000123C");
		assertNull(null);
	}
	
	@Test
	public void isValidCreatedAtTest1() {
		Audit audit = new Audit();
		LocalDateTime now = LocalDateTime.now();
		audit.setCreatedAt(now);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setAudit(audit);
		Audit auditDb = new Audit();
		auditDb.setCreatedAt(now);
		CommercialEntity commercialEntityDb = new CommercialEntity();
		commercialEntityDb.setAudit(auditDb);
		Mockito.when(financialRepository.findById("FBOC0000123C")).thenReturn(Optional.of(commercialEntityDb));
		validator.isValidCreatedAt(commercialEntity, "FBOC0000123C");
		assertNull(null);
	}

	@Test
	public void isValidCreatedAtNullTest2() {
		Audit audit = new Audit();
		audit.setCreatedAt(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setAudit(null);
		Audit auditDb = new Audit();
		auditDb.setCreatedAt(LocalDateTime.now());
		CommercialEntity commercialEntityDb = new CommercialEntity();
		commercialEntityDb.setAudit(auditDb);
		Mockito.when(financialRepository.findById("FBOC0000123C")).thenReturn(Optional.of(commercialEntityDb));
		validator.isValidCreatedAt(commercialEntity, "FBOC0000123C");
		assertNull(null);
	}

	@Test
	public void isValidCreatedAtNullTest3() {
		Audit audit = new Audit();
		audit.setCreatedAt(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40));
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setAudit(audit);
		Audit auditDb = new Audit();
		auditDb.setCreatedAt(LocalDateTime.now());
		CommercialEntity commercialEntityDb = new CommercialEntity();
		commercialEntityDb.setAudit(auditDb);
		Mockito.when(financialRepository.findById("FBOC0000123C")).thenReturn(Optional.empty());
		validator.isValidCreatedAt(commercialEntity, "FBOC0000123C");
		assertNull(null);
	}

	@Test
	public void isValidCreatedAtBothNullTest3() {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setAudit(null);
		CommercialEntity commercialEntityDb = new CommercialEntity();
		commercialEntityDb.setAudit(null);
		Mockito.when(financialRepository.findById("FBOC0000123C")).thenReturn(Optional.of(commercialEntityDb));
		validator.isValidCreatedAt(commercialEntity, "FBOC0000123C");
		assertNull(null);
	}

	@Test
	public void isValidCreatedAtEmptyTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setAudit(null);
		Mockito.when(financialRepository.findById("FBOC0000123C")).thenReturn(Optional.empty());
		validator.isValidCreatedAt(commercialEntity, "FBOC0000123C");
		assertNull(null);
	}

	@Test
	public void isBusinessTypePresentBusinessTypeNullTest() {
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setBusinessType(null);
		validator.isBusinessTypePresent(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isBusinessTypePresentTest() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigCode("LLC");
		businessType.setConfigId((long) 3000003);
		businessType.setConfigtypeCode(LOSEntityConstants.BUSINESS_TYPE);
		businessType.setConfigtypeId((long) 2000004);
		businessType.setConfigValue("LLC");
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setBusinessType(businessType);
		validator.isBusinessTypePresent(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isBusinessTypePresentConfigIdZeroTest() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigId((long) 0);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setBusinessType(businessType);
		validator.isBusinessTypePresent(commercialEntity);
		assertNull(null);
	}

	@Test
	public void isBusinessTypePresentConfigIdNullTest() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigId(null);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setBusinessType(businessType);
		validator.isBusinessTypePresent(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isBusinessTypePresentOptEmptyTest() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigId((long) 3000003);
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setBusinessType(businessType);
		Mockito.when(configLookupRepository.findById(businessType.getConfigId())).thenReturn(Optional.empty());
		validator.isBusinessTypePresent(commercialEntity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isBusinessTypePresentWrongConfigcodeTest() {
		LosConfigDetails businessType = new LosConfigDetails();
		businessType.setConfigId((long) 3000003);
		businessType.setConfigtypeCode("TEST_TYPE");
		CommercialEntity commercialEntity = new CommercialEntity();
		commercialEntity.setBusinessType(businessType);
		Mockito.when(configLookupRepository.findById(businessType.getConfigId())).thenReturn(Optional.of(businessType));
		validator.isBusinessTypePresent(commercialEntity);
		assertNull(null);
	}

}
