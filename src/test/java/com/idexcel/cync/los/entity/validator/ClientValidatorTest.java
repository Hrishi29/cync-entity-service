package com.idexcel.cync.los.entity.validator;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.dao.ClientDetailsRepository;
import com.idexcel.cync.los.entity.exception.ResourceNotFoundException;
import com.idexcel.cync.los.entity.model.ClientEntity;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.FinancialEntity;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClientValidatorTest {

	@InjectMocks
	public ClientValidator validator;

	@MockBean
	private ClientDetailsRepository clientDetailsRepository;
	@MockBean
	private EntityRelationshipType entityRelationshipType;

	@MockBean
	MessageSource errorMsgs;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		this.validator = new ClientValidator(clientDetailsRepository, errorMsgs);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void isClientExistInDbTest() {
		Long clientId = (long) 1001;
		MDC.put(LOSEntityConstants.CLIENT_ID_KEY, "1001");
		when(clientDetailsRepository.findById(clientId)).thenReturn(Optional.empty());
		validator.isClientExistInDb();
		assertNull(null);
	}

	@Test
	public void isValidClientFinancialEntityTest() {
		MDC.put(LOSEntityConstants.CLIENT_ID_KEY, "1001");
		ClientEntity clientDetail = new ClientEntity();
		clientDetail.setClientCode("FBOC");
		clientDetail.setClientId((long) 1001);
		clientDetail.setClientName("xyz");
		clientDetail.setStatus(true);
		FinancialEntity entity = new FinancialEntity();
		entity.setClientDetail(clientDetail);
		Mockito.when(clientDetailsRepository.findById(clientDetail.getClientId()))
				.thenReturn(Optional.of(clientDetail));
		validator.isValidClient(entity);
		assertNull(null);
	}

	@Test
	public void isValidClientEntityRelationshipTypeTest() {
		MDC.put(LOSEntityConstants.CLIENT_ID_KEY, "1001");
		ClientEntity clientDetail = new ClientEntity();
		clientDetail.setClientCode("FBOC");
		clientDetail.setClientId((long) 1001);
		clientDetail.setClientName("xyz");
		clientDetail.setStatus(true);
		EntityRelationshipType entityRelationshipType = new EntityRelationshipType();
		entityRelationshipType.setClientDetail(clientDetail);
		Mockito.when(clientDetailsRepository.findById(clientDetail.getClientId()))
				.thenReturn(Optional.of(clientDetail));
		validator.isValidClient(entityRelationshipType);
		assertNull(null);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void isValidClientTest1() {
		MDC.put(LOSEntityConstants.CLIENT_ID_KEY, "1001");
		FinancialEntity entity = new FinancialEntity();
		entity.setClientDetail(null);
		Mockito.when(clientDetailsRepository.findById(null)).thenReturn(Optional.empty());
		validator.isValidClient(entity);
		assertNull(null);
	}

}
