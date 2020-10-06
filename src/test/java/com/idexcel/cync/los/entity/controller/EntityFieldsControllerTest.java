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
import com.idexcel.cync.los.entity.dto.EntityFieldNameValue;
import com.idexcel.cync.los.entity.service.DBQueriesService;
import com.idexcel.cync.los.entity.service.EntityFieldsService;
import com.idexcel.cync.los.entity.utils.CommonData;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = EntityFieldsController.class, secure = false)
public class EntityFieldsControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	DBQueriesService dbQueriesService;

	@MockBean
	EntityConfig EntityConfig;

	@MockBean
	EntityFieldsService entityFieldsService;

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getEntityFields() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CommonData.ORIGIN_HOST_NAME+"entityFields")
//			.header("Authorization", CommonData.Token)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		List<EntityFieldNameValue> entityFieldNameValueList = new ArrayList<EntityFieldNameValue>();
		EntityFieldNameValue entityFieldNameValue = new EntityFieldNameValue();
		entityFieldNameValue.setFieldName("Test");
		entityFieldNameValue.setFieldValue("Test");
		entityFieldNameValueList.add(entityFieldNameValue);
		EntityFieldNameValue entityFieldNameValue1 = new EntityFieldNameValue();
		entityFieldNameValue1.setFieldName("Test1");
		entityFieldNameValue1.setFieldValue("Test1");
		entityFieldNameValueList.add(entityFieldNameValue1);
		Mockito.when(entityFieldsService.getEntityFields()).thenReturn(entityFieldNameValueList);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}
}
