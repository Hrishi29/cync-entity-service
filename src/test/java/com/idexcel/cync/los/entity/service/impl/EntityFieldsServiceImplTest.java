package com.idexcel.cync.los.entity.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.idexcel.cync.los.entity.dto.EntityFieldNameValue;

public class EntityFieldsServiceImplTest {

	@InjectMocks
	EntityFieldsServiceImpl serviceImpl;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getEntityFields() {
		List<EntityFieldNameValue> entityFieldNameValueList = new ArrayList<EntityFieldNameValue>();
		EntityFieldNameValue entityFieldNameValue = new EntityFieldNameValue();
		entityFieldNameValue.setFieldName("Address");
		entityFieldNameValue.setFieldValue("address");
		EntityFieldNameValue entityFieldNameValue1 = new EntityFieldNameValue();
		entityFieldNameValue1.setFieldName("createdBy");
		entityFieldNameValue1.setFieldValue("CreatedBy");
		entityFieldNameValueList.add(entityFieldNameValue);
		serviceImpl.getEntityFields();
		assertEquals(entityFieldNameValueList, entityFieldNameValueList);
	}
}
