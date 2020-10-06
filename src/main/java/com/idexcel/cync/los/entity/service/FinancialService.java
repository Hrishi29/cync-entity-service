package com.idexcel.cync.los.entity.service;

import java.util.List;

import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntityList;
import com.idexcel.cync.los.entity.model.LosConfigDetails;

/**
 * 
 * @author Amit
 *
 */
public interface FinancialService {

	public String saveEntity(FinancialEntity entity, Long entityTypeId);

	public FinancialEntity findById(String id);

	public void deactivateEntity(String entityId);

	public void activateEntity(String entityId);

	public String updateEntity(Long entityTypeId, String entityId, FinancialEntity entity);

	public List<FinancialEntity> searchEntityByName(String entityName);

	public List<FinancialEntityList> findAllEntity(LosConfigDetails losConfigdetails);

	public List<FinancialEntity> searchDuplicateEntityName(Long entityTypeId, String businessName, String firstName,
			String middleName, String lastName);

}
