package com.idexcel.cync.los.entity.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.idexcel.cync.los.entity.dto.CommercialEntityDto;
import com.idexcel.cync.los.entity.dto.EntityFieldNameValue;
import com.idexcel.cync.los.entity.dto.FinancialEntityDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityDto;
import com.idexcel.cync.los.entity.dto.NaicsDto;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.StateLookup;
import com.idexcel.cync.los.entity.service.EntityFieldsService;

@Service
public class EntityFieldsServiceImpl implements EntityFieldsService {
	
	List<Field[]> fieldList = Arrays.asList(CommercialEntityDto.class.getDeclaredFields(),
			IndividualEntityDto.class.getDeclaredFields(), FinancialEntityDto.class.getDeclaredFields(),
			CountryLookup.class.getDeclaredFields(), StateLookup.class.getDeclaredFields(),
			LosConfigDetails.class.getDeclaredFields(), NaicsDto.class.getDeclaredFields());

	/**
	 * Method to Get All the Declared fields in the respective Classes.
	 */
	@Override
	public List<EntityFieldNameValue> getEntityFields() {
		Map<String, EntityFieldNameValue> hashMap = new HashMap<>();
		fieldList.parallelStream().forEachOrdered(fieldArray -> {
			for (Field field : fieldArray) {
				addToList(field, hashMap);
			}
		});
		List<EntityFieldNameValue> list = new ArrayList<>(hashMap.values());
		list.sort(
				(EntityFieldNameValue f1, EntityFieldNameValue f2) -> f1.getFieldValue().compareTo(f2.getFieldValue()));
		return list;
	}

	/**
	 * Method to Filter the unwanted fieldNames for RiskRating and capitalizing the
	 * initial character.
	 * 
	 * @param field
	 * @param hashMap
	 */
	private void addToList(Field field, Map<String, EntityFieldNameValue> hashMap) {
		List<String> unwantedFields = Arrays.asList("serialVersionUID", "relationshipManagerId", "status", "createdAt",
				"createdBy", "modifiedBy", "modifiedAt", "map", "audit", "rmObject", "notes", "phone", "email",
				"entityTypeConfig", "businessType", "countryLookup", "country", "countryId", "state", "stateId",
				"configtypeId", "configId", "naics", "naicsCodeId");
		if (!unwantedFields.contains(field.getName())
				&& !hashMap.containsKey(addspace(StringUtils.capitalize(field.getName())))) {
			EntityFieldNameValue entityFieldNameValue = new EntityFieldNameValue();
			entityFieldNameValue.setFieldName(addspace(StringUtils.capitalize(field.getName())));
			entityFieldNameValue.setFieldValue(field.getName());
			hashMap.put(entityFieldNameValue.getFieldName(), entityFieldNameValue);
		}
	}

	/**
	 * Method to add space in front of Capital Letters present in entityFieldName
	 * 
	 * @param entityFieldName
	 * @return
	 */
	private String addspace(String entityFieldName) {
		StringBuilder out = new StringBuilder(entityFieldName);
		Matcher m = Pattern.compile("[A-Z]").matcher(entityFieldName);
		int addSpace = 0;
		while (m.find()) {
			if (m.start() != 0) {
				out = out.insert(m.start() + addSpace, " ");
				addSpace++;
			}
		}
		return out.toString();
	}
}