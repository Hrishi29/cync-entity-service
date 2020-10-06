package com.idexcel.cync.los.entity.controller;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.config.EntityConfig;
import com.idexcel.cync.los.entity.dto.CommercialEntityDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityDto;
import com.idexcel.cync.los.entity.mapper.FinancialEntityListMapper;
import com.idexcel.cync.los.entity.mapper.FinancialEntityMapper;
import com.idexcel.cync.los.entity.model.ClientEntity;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.CommercialEntityList;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntityList;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntityList;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.service.DBQueriesService;
import com.idexcel.cync.los.entity.service.FinancialService;
import com.idexcel.cync.los.entity.utils.CommonData;
import com.idexcel.cync.los.entity.validator.EntityDtoValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = EntityController.class, secure = false)
public class EntityControllerTest {

	public static final String INDIVIDUAL_ENTITY_DTO = "/test-los-entity/individual-entity-creation.json";
	public static final String COMMERCIAL_ENTITY_DTO = "/test-los-entity/commercial-entity-creation.json";

	@Autowired
	MockMvc mockMvc;

	@MockBean
	EntityConfig financeConfig;

	@MockBean
	private FinancialService financialService;

	@MockBean
	DBQueriesService dbQueriesService;

	@MockBean
	FinancialEntityMapper financialEntityMapper;

	@MockBean
	EntityDtoValidator entityDtoValidator;
	
	@MockBean
	FinancialEntityListMapper financialEntityListMapper;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getAllEntityList() throws Exception {
		List<FinancialEntityList> financialEntityList = new ArrayList<>();
		financialEntityList.add(new IndividualFinancialEntityList());
		financialEntityList.add(new CommercialEntityList());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.ENTITY_COMMON_URL)
				.contentType(MediaType.APPLICATION_JSON);
		Mockito.when(financialService.findAllEntity(null)).thenReturn(financialEntityList);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getAllCommercialEntityList() throws Exception {
		LosConfigDetails CommercialLosConfigdetails = new LosConfigDetails();
		CommercialLosConfigdetails.setConfigId(new Long(3000002));
		List<FinancialEntityList> commercialLfinancialEntity = new ArrayList<>();
		commercialLfinancialEntity.add(new CommercialEntityList());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.ENTITY_COMMON_URL)
				.contentType(MediaType.APPLICATION_JSON).param(new String("entityTypeId"), String.valueOf(3000002));
		Mockito.when(financialService.findAllEntity(CommercialLosConfigdetails)).thenReturn(commercialLfinancialEntity);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getAllIndividualEntityList() throws Exception {
		LosConfigDetails IndividualLosConfigdetails = new LosConfigDetails();
		IndividualLosConfigdetails.setConfigId(new Long(3000001));
		List<FinancialEntityList> individualfinancialEntity = new ArrayList<>();
		individualfinancialEntity.add(new IndividualFinancialEntityList());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.ENTITY_COMMON_URL)
				.contentType(MediaType.APPLICATION_JSON).param(new String("entityTypeId"), String.valueOf(3000001));
		Mockito.when(financialService.findAllEntity(IndividualLosConfigdetails)).thenReturn(individualfinancialEntity);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getEntityByCommercialEntityIdTest() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CommonData.ORIGIN_HOST_NAME + "entities/" + "FBOC000000C").contentType(MediaType.APPLICATION_JSON);
		Mockito.when(financialService.findById("FBOC000000C")).thenReturn(new CommercialEntity());
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getEntityByIndividualFinancialEntityIdTest() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CommonData.ORIGIN_HOST_NAME + "entities/" + "FBOC000000I").contentType(MediaType.APPLICATION_JSON);
		Mockito.when(financialService.findById("FBOC000000I")).thenReturn(new IndividualFinancialEntity());
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void createCommercialEntityTest() throws Exception {
		URL url = Resources.getResource(EntityControllerTest.class, COMMERCIAL_ENTITY_DTO);
		String content = Resources.toString(url, Charsets.UTF_8);
		ClientEntity clientInfo = new ClientEntity();
		clientInfo.setClientId(new Long(1001));
		clientInfo.setClientName("xyz");
		MDC.put(LOSEntityConstants.CLIENT_NAME_KEY, "xyz");
		Mockito.when(dbQueriesService.fetchClientByName("xyz")).thenReturn(clientInfo);
		Long entityTypeId = new Long(3000002);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(CommonData.ENTITY_COMMON_URL + "/commercial/" + entityTypeId).accept(MediaType.APPLICATION_JSON)
				.content(content).contentType(MediaType.APPLICATION_JSON);
		Mockito.when(financialService.saveEntity(financialEntityMapper.toCommercialEntity(new CommercialEntityDto()),
				entityTypeId)).thenReturn("FBOC000000C");
		assertEquals(HttpStatus.CREATED.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void createIndividualEntityTest() throws Exception {
		URL url = Resources.getResource(EntityControllerTest.class, INDIVIDUAL_ENTITY_DTO);
		String content = Resources.toString(url, Charsets.UTF_8);
		ClientEntity clientInfo = new ClientEntity();
		clientInfo.setClientId(new Long(1001));
		clientInfo.setClientName("xyz");
		MDC.put(LOSEntityConstants.CLIENT_NAME_KEY, "xyz");
		Mockito.when(dbQueriesService.fetchClientByName("xyz")).thenReturn(clientInfo);
		Long entityTypeId = new Long(3000001);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(CommonData.ENTITY_COMMON_URL + "/individual/" + entityTypeId).accept(MediaType.APPLICATION_JSON)
				.content(content).contentType(MediaType.APPLICATION_JSON);
		Mockito.when(financialService
				.saveEntity(financialEntityMapper.toIndividualFinancialEntity(new IndividualEntityDto()), entityTypeId))
				.thenReturn("FBOC000000I");
		assertEquals(HttpStatus.CREATED.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void updateCommercialEntityTest() throws Exception {
		URL url = Resources.getResource(EntityControllerTest.class, COMMERCIAL_ENTITY_DTO);
		String content = Resources.toString(url, Charsets.UTF_8);
		ClientEntity clientInfo = new ClientEntity();
		clientInfo.setClientId(new Long(1001));
		clientInfo.setClientName("xyz");
		MDC.put(LOSEntityConstants.CLIENT_NAME_KEY, "xyz");
		Mockito.when(dbQueriesService.fetchClientByName("xyz")).thenReturn(clientInfo);
		Long entityTypeId = new Long(3000002);
		String entityId = new String("FBOC000000C");
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(CommonData.ENTITY_COMMON_URL + "/commercial/" + entityTypeId + "/" + entityId)
				.accept(MediaType.APPLICATION_JSON).content(content).contentType(MediaType.APPLICATION_JSON);
		Mockito.when(financialService.updateEntity(new Long(3000002), entityId,
				financialEntityMapper.toCommercialEntity(new CommercialEntityDto()))).thenReturn("FBOC000000C");
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void updateIndividualEntityTest() throws Exception {
		URL url = Resources.getResource(EntityControllerTest.class, INDIVIDUAL_ENTITY_DTO);
		String content = Resources.toString(url, Charsets.UTF_8);
		ClientEntity clientInfo = new ClientEntity();
		clientInfo.setClientId(new Long(1001));
		clientInfo.setClientName("xyz");
		MDC.put(LOSEntityConstants.CLIENT_NAME_KEY, "xyz");
		Mockito.when(dbQueriesService.fetchClientByName("xyz")).thenReturn(clientInfo);
		Long entityTypeId = new Long(3000001);
		String entityId = new String("FBOC000000I");
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put(CommonData.ENTITY_COMMON_URL + "/individual/" + entityTypeId + "/" + entityId)
				.accept(MediaType.APPLICATION_JSON).content(content).contentType(MediaType.APPLICATION_JSON);
		Mockito.when(financialService.updateEntity(new Long(3000001), entityId,
				financialEntityMapper.toIndividualFinancialEntity(new IndividualEntityDto())))
				.thenReturn("FBOC000000I");
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void searchCommercialDuplicateEntityNameTest() throws Exception {
		List<FinancialEntity> financialEntityList = new ArrayList<FinancialEntity>();
		financialEntityList.add(new CommercialEntity());
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CommonData.ENTITY_COMMON_URL + "/searchDuplicateEntityName")
				.contentType(MediaType.APPLICATION_JSON).param(new String("entityTypeId"), String.valueOf(3000002))
				.param(new String("businessName"), new String("Commercial"));
		Mockito.when(financialService.searchDuplicateEntityName(new Long(3000002), new String("Commercial"), null, null,
				null)).thenReturn(financialEntityList);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void searchIndividualDuplicateEntityNameTest() throws Exception {
		List<FinancialEntity> financialEntityList = new ArrayList<FinancialEntity>();
		financialEntityList.add(new IndividualFinancialEntity());
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CommonData.ENTITY_COMMON_URL + "/searchDuplicateEntityName")
				.contentType(MediaType.APPLICATION_JSON).param(new String("entityTypeId"), String.valueOf(3000001))
				.param(new String("firstName"), new String("FirstName"))
				.param(new String("middleName"), new String("M")).param(new String("lastName"), new String("LastName"));
		Mockito.when(financialService.searchDuplicateEntityName(new Long(3000001), null, new String("FirstName"),
				new String("M"), new String("LastName"))).thenReturn(financialEntityList);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void searchCommercialEntityByNameTest() throws Exception {
		List<FinancialEntity> financialEntityList = new ArrayList<FinancialEntity>();
		financialEntityList.add(new CommercialEntity());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.ENTITY_COMMON_URL + "/nameSearch")
				.contentType(MediaType.APPLICATION_JSON).param(new String("entityName"), new String("Test"));
		Mockito.when(financialService.searchEntityByName(new String("Test"))).thenReturn(financialEntityList);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void searchIndividualEntityByNameTest() throws Exception {
		List<FinancialEntity> financialEntityList = new ArrayList<FinancialEntity>();
		financialEntityList.add(new IndividualFinancialEntity());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.ENTITY_COMMON_URL + "/nameSearch")
				.contentType(MediaType.APPLICATION_JSON).param(new String("entityName"), new String("Test"));
		Mockito.when(financialService.searchEntityByName(new String("Test"))).thenReturn(financialEntityList);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void findClientCodeTest() throws Exception {
		URL url = Resources.getResource(EntityControllerTest.class, COMMERCIAL_ENTITY_DTO);
		String content = Resources.toString(url, Charsets.UTF_8);
		ClientEntity clientInfo = new ClientEntity();
		clientInfo.setClientId(new Long(1001));
		clientInfo.setClientName("xyz");
		MDC.put(LOSEntityConstants.CLIENT_NAME_KEY, null);
		Mockito.when(dbQueriesService.fetchClientByName("xyz")).thenReturn(null);
		Long entityTypeId = new Long(3000002);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(CommonData.ENTITY_COMMON_URL + "/commercial/" + entityTypeId).accept(MediaType.APPLICATION_JSON)
				.content(content).contentType(MediaType.APPLICATION_JSON);
		Mockito.when(financialService.saveEntity(financialEntityMapper.toCommercialEntity(new CommercialEntityDto()),
				entityTypeId)).thenReturn("FBOC000000C");
		assertEquals(HttpStatus.NOT_FOUND.value(),
				mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}
	
}
