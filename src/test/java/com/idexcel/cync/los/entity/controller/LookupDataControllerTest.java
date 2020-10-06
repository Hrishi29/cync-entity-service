package com.idexcel.cync.los.entity.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.idexcel.cync.los.entity.config.EntityConfig;
import com.idexcel.cync.los.entity.dto.CountryLookupDto;
import com.idexcel.cync.los.entity.dto.CountryStateLookupDto;
import com.idexcel.cync.los.entity.dto.LosConfigTypeDto;
import com.idexcel.cync.los.entity.dto.StateLookupDto;
import com.idexcel.cync.los.entity.mapper.LookupDataMapper;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.LosConfigType;
import com.idexcel.cync.los.entity.model.StateLookup;
import com.idexcel.cync.los.entity.service.ConfigLookupService;
import com.idexcel.cync.los.entity.service.CountryLookupService;
import com.idexcel.cync.los.entity.service.StateLookupService;
import com.idexcel.cync.los.entity.utils.CommonData;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = LookupDataController.class, secure = false)
public class LookupDataControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	EntityConfig financeConfig;

	@MockBean
	private CountryLookupService countryLookupService;

	@MockBean
	private StateLookupService stateLookupService;

	@MockBean
	private LookupDataMapper lookupDataMapper;

	@MockBean
	private ConfigLookupService configLookupService;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getAllCountryTest() throws Exception {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId((long) 4000000);
		countryLookup.setCountryCode("ASM");
		countryLookup.setCountryName("American Samoa");
		List<CountryLookup> countryList = new ArrayList<CountryLookup>();
		countryList.add(countryLookup);
		CountryLookupDto countryLookupDto = new CountryLookupDto();
		countryLookupDto.setCountryId((long) 4000000);
		countryLookupDto.setCountryCode("ASM");
		countryLookupDto.setCountryName("American Samoa");
		List<CountryLookupDto> countryLookupDtoList = new ArrayList<CountryLookupDto>();
		countryLookupDtoList.add(countryLookupDto);
		Mockito.when(countryLookupService.findCountryList()).thenReturn(countryList);
		Mockito.when(lookupDataMapper.convertToListCountryLookupDto(countryList)).thenReturn(countryLookupDtoList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.LOOKUP_COMMON_URL + "countries")
				.accept(MediaType.APPLICATION_JSON);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getAllStateForCountryIdTest() throws Exception {
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateCode("AB");
		stateLookup.setStateId((long) 5000054);
		stateLookup.setStateName("Alberta");
		List<StateLookup> stateList = new ArrayList<StateLookup>();
		stateList.add(stateLookup);
		StateLookupDto stateLookupDto = new StateLookupDto();
		stateLookupDto.setStateCode("AB");
		stateLookupDto.setStateId((long) 5000054);
		stateLookupDto.setStateName("Alberta");
		Long countryId = new Long(4000002);
		Mockito.when(stateLookupService.findStateList((long) 777777)).thenReturn(stateList);
		Mockito.when(lookupDataMapper.toStateLookupDto(stateLookup)).thenReturn(stateLookupDto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.LOOKUP_COMMON_URL + countryId + "/states")
				.accept(MediaType.APPLICATION_JSON);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getAllStatesAndCountries() throws Exception {
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateCode("AB");
		stateLookup.setStateId((long) 5000054);
		stateLookup.setStateName("Alberta");
		List<StateLookup> stateList = new ArrayList<StateLookup>();
		stateList.add(stateLookup);
		CountryStateLookupDto countryStateLookupDto = new CountryStateLookupDto();
		countryStateLookupDto.setCountryId(new Long(4000023));
		countryStateLookupDto.setCountryName("United States");
		countryStateLookupDto.setCountryCode("USA");
		countryStateLookupDto.setStateId(new Long(5000002));
		countryStateLookupDto.setStateCode("AZ");
		countryStateLookupDto.setStateName("Arizona");
		Mockito.when(stateLookupService.stateList()).thenReturn(stateList);
		Mockito.when(lookupDataMapper.toCountryStateLookupDto(stateLookup)).thenReturn(countryStateLookupDto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.LOOKUP_COMMON_URL + "CountryStateLookup")
				.accept(MediaType.APPLICATION_JSON);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getConfigListBasedOnconfigtypeCode() throws Exception {
		LosConfigDetails losConfigDetails = new LosConfigDetails();
		losConfigDetails.setConfigId(new Long(3000001));
		losConfigDetails.setConfigCode("INDIVIDUAL");
		losConfigDetails.setConfigtypeId(new Long(2000003));
		losConfigDetails.setConfigtypeCode("ENTITY");
		losConfigDetails.setConfigValue("Individual");
		List<LosConfigDetails> losConfigDetailsList = new ArrayList<LosConfigDetails>();
		losConfigDetailsList.add(losConfigDetails);
		LosConfigType losConfigType = new LosConfigType();
		losConfigType.setClientId(new Long(1001));
		losConfigType.setConfigtypeCode("ENTITY");
		losConfigType.setConfigTypeId(new Long(2000003));
		losConfigType.setConfigtypeName("Entity type");
		List<LosConfigType> losConfigTypeList = new ArrayList<LosConfigType>();
		losConfigTypeList.add(losConfigType);
		LosConfigTypeDto losConfigTypeDto = new LosConfigTypeDto();
		losConfigTypeDto.setClientId(new Long(1001));
		losConfigTypeDto.setConfigtypeCode("ENTITY");
		losConfigTypeDto.setConfigTypeId(new Long(2000003));
		losConfigTypeDto.setConfigtypeName("Entity type");
		List<LosConfigTypeDto> losConfigTypeDtoList = new ArrayList<LosConfigTypeDto>();
		losConfigTypeDtoList.add(losConfigTypeDto);
		Mockito.when(configLookupService.findConfigDetails(new String("Entity"))).thenReturn(losConfigDetailsList);
		Mockito.when(lookupDataMapper.toLosConfigTypeDto(losConfigTypeList)).thenReturn(losConfigTypeDtoList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CommonData.LOOKUP_COMMON_URL + "configType/" + new String("Entity"))
				.accept(MediaType.APPLICATION_JSON);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getAllConfigTypes() throws Exception {
		LosConfigType losConfigType = new LosConfigType();
		losConfigType.setClientId(new Long(1001));
		losConfigType.setConfigtypeCode("ENTITY");
		losConfigType.setConfigTypeId(new Long(2000003));
		losConfigType.setConfigtypeName("Entity type");
		List<LosConfigType> losConfigTypeList = new ArrayList<LosConfigType>();
		losConfigTypeList.add(losConfigType);
		LosConfigTypeDto losConfigTypeDto = new LosConfigTypeDto();
		losConfigTypeDto.setClientId(new Long(1001));
		losConfigTypeDto.setConfigtypeCode("ENTITY");
		losConfigTypeDto.setConfigTypeId(new Long(2000003));
		losConfigTypeDto.setConfigtypeName("Entity type");
		List<LosConfigTypeDto> losConfigTypeDtoList = new ArrayList<LosConfigTypeDto>();
		losConfigTypeDtoList.add(losConfigTypeDto);
		Mockito.when(configLookupService.findAllConfigTypes()).thenReturn(losConfigTypeList);
		Mockito.when(lookupDataMapper.toLosConfigTypeDto(losConfigTypeList)).thenReturn(losConfigTypeDtoList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.LOOKUP_COMMON_URL + "configTypes")
				.accept(MediaType.APPLICATION_JSON);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

}
