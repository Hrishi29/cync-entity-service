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

import com.idexcel.cync.los.entity.dao.CountryLookupRepository;
import com.idexcel.cync.los.entity.model.CountryLookup;

@RunWith(SpringJUnit4ClassRunner.class)
public class CountryLookupServiceImplTest {
	
	@InjectMocks
	private CountryLookupServiceImpl serviceImpl;

	@MockBean
	private CountryLookupRepository countryLookupRepository;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void findConfigDetails() throws Exception {
		CountryLookup countryLookup1 = new CountryLookup();
		countryLookup1.setCountryCode("CAN");
		countryLookup1.setCountryId(new Long(4000002));
		countryLookup1.setCountryName("Canada");
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryCode("ASM");
		countryLookup.setCountryId(new Long(4000000));
		countryLookup.setCountryName("American Samoa");
		List<CountryLookup> countryLookupList = new ArrayList<CountryLookup>();
		countryLookupList.add(countryLookup);
		countryLookupList.add(countryLookup1);
		Mockito.when(countryLookupRepository.findAll(Sort.by(Sort.Order.asc(countryLookup.getCountryName())))).thenReturn(countryLookupList);
		serviceImpl.findCountryList();
		assertEquals(countryLookupList,countryLookupList);
	}

}
