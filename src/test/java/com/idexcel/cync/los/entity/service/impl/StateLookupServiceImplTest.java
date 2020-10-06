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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idexcel.cync.los.entity.dao.StateLookupRepository;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.model.StateLookup;
import com.idexcel.cync.los.entity.validator.FSCountryStateValidator;

@RunWith(SpringJUnit4ClassRunner.class)
public class StateLookupServiceImplTest {

	@InjectMocks
	StateLookupServiceImpl serviceImpl;
	
	@MockBean
	private StateLookupRepository stateLookupRepository;
	
	@MockBean
	private FSCountryStateValidator fSCountryStateValidator;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void findConfigDetails() throws Exception {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId(new Long(4000002));
		countryLookup.setCountryCode("CAN");
		countryLookup.setCountryName("Canada");
		List<StateLookup> stateLookupList = new ArrayList<StateLookup>();
		StateLookup stateLookup = new StateLookup();
		stateLookup.setCountryId(new Long(4000002));
		stateLookup.setCountryLookup(countryLookup);
		stateLookup.setStateCode("BC");
		stateLookup.setStateId(new Long(5000055));
		stateLookup.setStateName("British Columbia");
		StateLookup stateLookup1 = new StateLookup();
		stateLookup1.setCountryId(new Long(4000002));
		stateLookup1.setCountryLookup(countryLookup);
		stateLookup1.setStateCode("MB");
		stateLookup1.setStateId(new Long(5000056));
		stateLookup1.setStateName("Manitoba");
		stateLookupList.add(stateLookup);
		stateLookupList.add(stateLookup1);
		fSCountryStateValidator.isValidCountryId(countryLookup.getCountryId());
		Mockito.when(stateLookupRepository.findByCountryId(stateLookup.getCountryId(), Sort.by(Sort.Order.asc(stateLookup.getStateName())))).thenReturn(stateLookupList);
		serviceImpl.findStateList(countryLookup.getCountryId());
		assertEquals(stateLookupList,stateLookupList);
	}
	
	@Test
	public void stateList() {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId(new Long(4000002));
		countryLookup.setCountryCode("CAN");
		countryLookup.setCountryName("Canada");
		List<StateLookup> stateLookupList = new ArrayList<StateLookup>();
		StateLookup stateLookup = new StateLookup();
		stateLookup.setCountryId(new Long(4000002));
		stateLookup.setCountryLookup(countryLookup);
		stateLookup.setStateCode("BC");
		stateLookup.setStateId(new Long(5000055));
		stateLookup.setStateName("British Columbia");
		StateLookup stateLookup1 = new StateLookup();
		stateLookup1.setCountryId(new Long(4000002));
		stateLookup1.setCountryLookup(countryLookup);
		stateLookup1.setStateCode("MB");
		stateLookup1.setStateId(new Long(5000056));
		stateLookup1.setStateName("Manitoba");
		stateLookupList.add(stateLookup);
		stateLookupList.add(stateLookup1);
		Mockito.when(stateLookupRepository.findAll(Sort.by(Sort.Order.asc("stateName")))).thenReturn(stateLookupList);
		serviceImpl.stateList();
		assertEquals(stateLookupList,stateLookupList);
	}
}
