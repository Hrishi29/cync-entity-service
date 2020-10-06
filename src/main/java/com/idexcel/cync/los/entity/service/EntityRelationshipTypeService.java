package com.idexcel.cync.los.entity.service;

import java.util.List;

import com.idexcel.cync.los.entity.dto.EntityNodeSpouseDto;
import com.idexcel.cync.los.entity.model.EntityNode;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.ParentEntityNode;

public interface EntityRelationshipTypeService {

	public Long saveEntityRelation(EntityRelationshipType entityRelationshipType, String entityId);

	public EntityNode findEntityRelationTreeByEntityId(String entityId);

	public List<EntityRelationshipType> findEntityRelationListByEntityId(String entityId);
	
	public Long updateEntityRelaton(Long id, EntityRelationshipType entityRelationshipType);
	
	public void deactivateEntityRelationship(Long id);

	public void activateEntityRelationship(Long id);
	
	public Long findParentEntityCount(String entityId1);
	
	public Long findChildEntityCount(String entityId2);
	
	public ParentEntityNode findEntityRelationTreeByParentEntityId(String entityId1);
	
	public void deleteEntityRelation(Long id);

	public List<EntityRelationshipType> findEntityRelationsListByIds(List<String>  entityIds);

	public EntityNodeSpouseDto findSpouse(String entityId);

}