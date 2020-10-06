package com.idexcel.cync.los.entity.controller;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.idexcel.cync.los.entity.config.EntityConfig;
import com.idexcel.cync.los.entity.dao.ParentEntityNodeRepository;
import com.idexcel.cync.los.entity.dto.EntityNodeDto;
import com.idexcel.cync.los.entity.dto.EntityNodeSpouseDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipBorrowerDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipTypeDto;
import com.idexcel.cync.los.entity.mapper.EntityRelationshipTypeMapper;
import com.idexcel.cync.los.entity.service.DBQueriesService;
import com.idexcel.cync.los.entity.service.EntityRelationshipTypeService;
import com.idexcel.cync.los.entity.utils.CommonData;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = EntityRelationshipController.class, secure = false)
public class EntityRelationshipControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	EntityConfig financeConfig;

	@MockBean
	DBQueriesService dbQueriesService;

	@MockBean
	ParentEntityNodeRepository parentEntityNodeRepository;

	@MockBean
	EntityRelationshipTypeMapper entityRelationshipTypeMapper;

	@MockBean
	EntityRelationshipTypeService entityRelationshipTypeService;

	@Test
	public void createEntityRelationTest() throws Exception {
		URL url = Resources.getResource(EntityControllerTest.class, CommonData.ENTITY_RELATION_JSON);
		String content = Resources.toString(url, Charsets.UTF_8);
		EntityRelationshipDto entityRelationshipDto = new EntityRelationshipDto();
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(CommonData.ENTITY_RELATION_COMMON_URL + "FBOC000000C").contentType(MediaType.APPLICATION_JSON)
				.content(content);
		Mockito.when(entityRelationshipTypeService.saveEntityRelation(
				entityRelationshipTypeMapper.toEntityRelationshipCreteType(entityRelationshipDto),
				new String("FBOC000000C"))).thenReturn(new Long(2));
		assertEquals(HttpStatus.CREATED.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getEntityTreeByIdTest() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CommonData.ENTITY_RELATION_COMMON_URL + "tree/FBOC000000C")
				.contentType(MediaType.APPLICATION_JSON);
		Mockito.when(entityRelationshipTypeMapper.toEntityNodeDto(
				entityRelationshipTypeService.findEntityRelationTreeByParentEntityId(new String("FBOC000000C"))))
				.thenReturn(new EntityNodeDto());
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getEntityListByIdTest() throws Exception {
		List<EntityRelationshipTypeDto> entityRelationshipTypeDtoList = new ArrayList<EntityRelationshipTypeDto>();
		EntityRelationshipTypeDto entityRelationshipTypeDto = new EntityRelationshipTypeDto();
		entityRelationshipTypeDtoList.add(entityRelationshipTypeDto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CommonData.ENTITY_RELATION_COMMON_URL + "FBOC000000C").contentType(MediaType.APPLICATION_JSON);
		Mockito.when(entityRelationshipTypeMapper.toEntityRelationshipTypeDto(
				entityRelationshipTypeService.findEntityRelationListByEntityId(new String("FBOC000000C"))))
				.thenReturn(entityRelationshipTypeDtoList);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void updateEntityRelatonTest() throws Exception {
		URL url = Resources.getResource(EntityControllerTest.class, CommonData.ENTITY_RELATION_JSON);
		String content = Resources.toString(url, Charsets.UTF_8);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(CommonData.ENTITY_RELATION_COMMON_URL + "1")
				.contentType(MediaType.APPLICATION_JSON).content(content);
		Mockito.when(entityRelationshipTypeService.updateEntityRelaton(new Long(2),
				entityRelationshipTypeMapper.toEntityRelationshipCreteType(new EntityRelationshipDto())))
				.thenReturn(new Long(1));
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void deleteEntityRelationByIdTest() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CommonData.ENTITY_RELATION_COMMON_URL + "1")
				.contentType(MediaType.APPLICATION_JSON);
		entityRelationshipTypeService.deleteEntityRelation(new Long(2));
		assertEquals(HttpStatus.NO_CONTENT.value(),
				mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getEntityRelationsListByIdsTest() throws Exception {
		List<EntityRelationshipBorrowerDto> entityRelationshipBorrowerDtoList = new ArrayList<EntityRelationshipBorrowerDto>();
		EntityRelationshipBorrowerDto entityRelationshipBorrowerDto = new EntityRelationshipBorrowerDto();
		entityRelationshipBorrowerDtoList.add(entityRelationshipBorrowerDto);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(CommonData.ENTITY_RELATION_COMMON_URL + "/borrowers").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsBytes(Arrays.asList("FBOC000001C")));
		Mockito.when(entityRelationshipTypeMapper.toEntityRelationshipBorrowerDto(
				entityRelationshipTypeService.findEntityRelationsListByIds(Arrays.asList("FBOC000001C"))))
				.thenReturn(entityRelationshipBorrowerDtoList);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

	@Test
	public void getEntitySpouseTest() throws Exception {
		EntityNodeSpouseDto spouseData = new EntityNodeSpouseDto();
		spouseData.setSpouseEntityID("FBOC000002I");
		spouseData.setFirstName("Test");
		spouseData.setLastName("Spouse");
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(CommonData.ENTITY_RELATION_COMMON_URL + "spouse/FBOC000001I")
				.contentType(MediaType.APPLICATION_JSON);
		Mockito.when(entityRelationshipTypeService.findSpouse("FBOC000001I")).thenReturn(spouseData);
		assertEquals(HttpStatus.OK.value(), mockMvc.perform(requestBuilder).andReturn().getResponse().getStatus());
	}

}
