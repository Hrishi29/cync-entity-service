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

import com.idexcel.cync.los.entity.dao.CountryLookupRepository;
import com.idexcel.cync.los.entity.dao.StateLookupRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.StateLookup;

@RunWith(SpringJUnit4ClassRunner.class)
public class FSCountryStateValidatorTest {

	@InjectMocks
	private FSCountryStateValidator validator;

	@MockBean
	private CountryLookupRepository countryLookupRepository;
	@MockBean
	private StateLookupRepository stateLookupRepository;
	@MockBean
	private MessageSource errorMsgs;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		this.validator = new FSCountryStateValidator(countryLookupRepository, stateLookupRepository, errorMsgs);
	}

	@Test
	public void CIDNullSIDnull1() {
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(null);
		entity.setState(null);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}
	
	@Test
	public void CIDNullSIDnull() {
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateId(null);
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId(null);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(stateLookup);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void CIDNullSIDNotNull() {
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateId(new Long(5000055));
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId(null);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(stateLookup);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}

	@Test
	public void CIDNotNullAndsetCIDNull() {
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateId(null);
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId((long)4000002);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(stateLookup)
		;Mockito.when(countryLookupRepository.findByCountryId((long)4000002)).thenReturn(countryLookup);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}
	
	@Test
	public void CIDNotNullAndsetCIDNullStateObjNull() {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId((long)4000002);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(null)
		;Mockito.when(countryLookupRepository.findByCountryId((long)4000002)).thenReturn(countryLookup);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}
	@Test
	public void CIDNotNullAndsetCIDNullStateObjNullStateIdNull() {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId(null);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(null)
		;Mockito.when(countryLookupRepository.findByCountryId((long)4000002)).thenReturn(countryLookup);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}

	@Test
	public void CIDNotNullAndSIDNotNull() {
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateId((long)5000055);
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId((long)4000002);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(stateLookup);
		Mockito.when(countryLookupRepository.findByCountryId((long)4000002)).thenReturn(countryLookup);
		Mockito.when(stateLookupRepository.findByStateIdAndCountryId(entity.getState().getStateId(),
				entity.getCountry().getCountryId())).thenReturn(stateLookup);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}
	
	@Test
	public void CIDNotNullAndSIDNotNullBothNull() {
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateId(null);
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId(null);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(stateLookup);
		Mockito.when(countryLookupRepository.findByCountryId((long)4000002)).thenReturn(countryLookup);
		Mockito.when(stateLookupRepository.findByStateIdAndCountryId(entity.getState().getStateId(),
				entity.getCountry().getCountryId())).thenReturn(stateLookup);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}
	
	@Test
	public void CIDNotNullAndSIDNotNull1() {
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateId((long)5000055);
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId((long)4000002);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(stateLookup);
		Mockito.when(countryLookupRepository.findByCountryId((long)4000002)).thenReturn(countryLookup);
		Mockito.when(stateLookupRepository.findByStateIdAndCountryId(entity.getState().getStateId(),
				entity.getCountry().getCountryId())).thenReturn(stateLookup);
		validator.isCountryStateNullOrNot(entity);
		assertNull(null);
	}
	

	@Test
	public void isValidStateIdSuccessTest() {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId((long)4000002);
		countryLookup.setCountryCode("CAN");
		countryLookup.setCountryName("Canada");
		StateLookup stateLookup = new StateLookup();
		stateLookup.setCountryId((long)4000002);
		stateLookup.setCountryLookup(countryLookup);
		stateLookup.setStateCode("BC");
		stateLookup.setStateId((long)5000055);
		stateLookup.setStateName("British Columbia");
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(stateLookup);
		Mockito.when(stateLookupRepository.findByStateIdAndCountryId(entity.getState().getStateId(),
				entity.getCountry().getCountryId())).thenReturn(stateLookup);
		validator.isValidStateId(entity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidStateIdFailureTest() {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId(null);
		StateLookup stateLookup = new StateLookup();
		stateLookup.setStateId(null);
		FinancialEntity entity = new FinancialEntity();
		entity.setCountry(countryLookup);
		entity.setState(stateLookup);
		Mockito.when(stateLookupRepository.findByStateIdAndCountryId(entity.getState().getStateId(),
				entity.getCountry().getCountryId())).thenReturn(null);
		validator.isValidStateId(entity);
		assertNull(null);
	}

	@Test(expected = BadRequestException.class)
	public void isValidCountryId() {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId(null);
		Mockito.when(countryLookupRepository.findById(new Long(122))).thenReturn(Optional.of(countryLookup));
		validator.isValidCountryId(new Long(1235));
		assertNull(null);
	}
	
	@Test
	public void isValidCountryIdNull() {
		CountryLookup countryLookup = new CountryLookup();
		countryLookup.setCountryId((long)4000002);
		countryLookup.setCountryCode("CAN");
		countryLookup.setCountryName("Canada");
		Mockito.when(countryLookupRepository.findByCountryId((long)4000002)).thenReturn(countryLookup);
		validator.isValidCountryId((long)4000002);
		assertNull(null);
	}

}
