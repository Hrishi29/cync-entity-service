package com.idexcel.cync.los.entity.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idexcel.cync.los.entity.dao.ConfigLookupRepository;
import com.idexcel.cync.los.entity.dao.LosConfigTypeRepository;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.LosConfigType;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigLookupServiceImplTest {

	@InjectMocks
	private ConfigLookupServiceImpl serviceImpl;

	@MockBean
	private ConfigLookupRepository configLookupRepository;

	@MockBean
	private LosConfigTypeRepository losConfigTypeRepository;

//	@MockBean
//	private LosEntityExceptionHandler losEntityExceptionHandler;
	
	@MockBean
	private MessageSource errorMsgs;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findConfigDetails() throws Exception {
		List<LosConfigType> configTypeCodeList = new ArrayList<LosConfigType>();
		LosConfigType losConfigType = new LosConfigType();
		losConfigType.setClientId(new Long(1001));
		losConfigType.setConfigtypeCode("entity");
		losConfigType.setConfigTypeId(new Long(3000003));
		losConfigType.setConfigtypeName("ENTITY");
		LosConfigType losConfigType1 = new LosConfigType();
		losConfigType1.setClientId(new Long(1001));
		losConfigType1.setConfigtypeCode("COUNTRY");
		losConfigType1.setConfigTypeId(new Long(3000001));
		losConfigType1.setConfigtypeName("County");
		LosConfigType losConfigType2 = new LosConfigType();
		losConfigType2.setClientId(new Long(1001));
		losConfigType2.setConfigtypeCode("STATE");
		losConfigType2.setConfigTypeId(new Long(3000002));
		losConfigType2.setConfigtypeName("State");
		configTypeCodeList.add(losConfigType);
		configTypeCodeList.add(losConfigType1);
		configTypeCodeList.add(losConfigType2);
		List<LosConfigDetails> losConfigDetailsList = new ArrayList<LosConfigDetails>();
		LosConfigDetails losConfigDetails = new LosConfigDetails();
		losConfigDetails.setConfigCode("INDIVIDUAL");
		losConfigDetails.setConfigId(new Long(3000001));
		losConfigDetails.setConfigtypeCode("ENTITY");
		losConfigDetails.setConfigtypeId(new Long(2000003));
		losConfigDetails.setConfigValue("Individual");
		losConfigDetailsList.add(losConfigDetails);
		Mockito.when(losConfigTypeRepository.findByConfigtypeCode("ENTITY")).thenReturn(configTypeCodeList);
		Mockito.when(configLookupRepository.findByConfigtypeCode("ENTITY")).thenReturn(losConfigDetailsList);
		assertEquals(serviceImpl.findConfigDetails(new String("ENTITY")),losConfigDetailsList);
	}

	@Test(expected = NullPointerException.class)
	public void findConfigDetailsNegativeCase()throws Exception  {
		List<LosConfigType> configTypeCodeList = new ArrayList<>();
		Mockito.when(losConfigTypeRepository.findByConfigtypeCode("TEST")).thenReturn(configTypeCodeList);
		serviceImpl.findConfigDetails(("test"));
	}

	@Test
	public void findAllConfigTypes() throws Exception {
		LosConfigType losConfigType = new LosConfigType();
		losConfigType.setClientId(new Long(1001));
		losConfigType.setConfigtypeCode("ENTITY");
		losConfigType.setConfigTypeId(new Long(3000003));
		losConfigType.setConfigtypeName("entity");
		LosConfigType losConfigType1 = new LosConfigType();
		losConfigType1.setClientId(new Long(1001));
		losConfigType1.setConfigtypeCode("COUNTRY");
		losConfigType1.setConfigTypeId(new Long(3000001));
		losConfigType1.setConfigtypeName("County");
		LosConfigType losConfigType2 = new LosConfigType();
		losConfigType2.setClientId(new Long(1001));
		losConfigType2.setConfigtypeCode("STATE");
		losConfigType2.setConfigTypeId(new Long(3000002));
		losConfigType2.setConfigtypeName("State");
		List<LosConfigType> losConfigTypeList = new ArrayList<LosConfigType>();
		losConfigTypeList.add(losConfigType);
		losConfigTypeList.add(losConfigType1);
		losConfigTypeList.add(losConfigType2);
		Mockito.when(losConfigTypeRepository.findAll()).thenReturn(losConfigTypeList);
		assertEquals(serviceImpl.findAllConfigTypes(),losConfigTypeList);
	}

}
