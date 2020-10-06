package com.idexcel.cync.los.entity.service.impl;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idexcel.cync.los.entity.dao.ClientDetailsRepository;
import com.idexcel.cync.los.entity.model.ClientEntity;

@RunWith(SpringJUnit4ClassRunner.class)
public class DBQueriesServiceImplTest {

	@InjectMocks
	DBQueriesServiceImpl serviceImpl;

	@MockBean
	private ClientDetailsRepository clientDetailsRepository;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		this.serviceImpl = new DBQueriesServiceImpl(clientDetailsRepository);
	}

	@Test
	public void findConfigDetailstest() throws Exception {
		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setClientCode("FBOC");
		clientEntity.setClientId(new Long(1001));
		clientEntity.setClientName("xyz");
		clientEntity.setStatus(true);
		Mockito.when(clientDetailsRepository.findByClientNameAndStatus("xyz", true)).thenReturn(clientEntity);
		serviceImpl.fetchClientByName(clientEntity.getClientName());
		assertNull(null);
	}

	@Test
	public void findConfigDetailsNullTest() throws Exception {
		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setClientName("xyz");
		Mockito.when(clientDetailsRepository.findByClientNameAndStatus("xyz", true)).thenReturn(null);
		serviceImpl.fetchClientByName(clientEntity.getClientName());
		assertNull(null);
	}

}
