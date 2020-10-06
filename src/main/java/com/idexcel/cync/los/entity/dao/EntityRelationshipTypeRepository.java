package com.idexcel.cync.los.entity.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idexcel.cync.los.entity.model.EntityRelationshipType;

@Repository
public interface EntityRelationshipTypeRepository extends JpaRepository<EntityRelationshipType, Long> {

	Optional<EntityRelationshipType> findByEntityId1AndEntityId2AndDeletedOrEntityId2AndEntityId1AndDeleted(
			String entityId1, String entityId2, boolean b, String entityId3, String entityId4, boolean bool);

	@Query(value = "WITH RECURSIVE entity as (SELECT entity_id1, entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id1 = ?1  And er.deleted =false "
			+ "UNION ALL SELECT er.entity_id1, er.entity_id2, er.entity_relationshiptype_id, er.client_id, er.relationshiptype_id, er.ownership, er.status, er.deleted,  er.created_by, er.created_at, er.modified_by, er.modified_at FROM enty.tb_entity_relationship_type_map er INNER JOIN entity et ON et.entity_id2 = er.entity_id1  AND et.deleted = false AND er.deleted=false) SELECT * FROM entity", nativeQuery = true)
	List<EntityRelationshipType> findEntityTreeData(String entityId1);

	EntityRelationshipType findByEntityId1AndEntityId2AndDeleted(String entityId1, String entityId2, boolean bool);

	EntityRelationshipType findByEntityId2AndEntityId1AndDeleted(String entityId2, String entityId1, boolean bool);

	@Query(value = "SELECT DISTINCT entity_id1, entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id1 IN(?1) And er.deleted =false AND er.status =true ", nativeQuery = true)
	List<EntityRelationshipType> findAllRelatedEntityRelationData(List<String> entityIds);

	@Modifying
	@Transactional
	@Query("update EntityRelationshipType e set e.status = false where e.entityRelationshiptypeId = :id")
	int deactivateEntityRelationship(Long id);

	@Modifying
	@Transactional
	@Query("update EntityRelationshipType e set e.status = true where e.entityRelationshiptypeId = :id")
	int activateEntityRelationship(Long id);

	@Query("SELECT count(*) FROM EntityRelationshipType WHERE entityId2 =?1")
	int findByEntityId2(String entityId2);

	@Modifying
	@Transactional
	@Query("update EntityRelationshipType e set e.status = false, e.deleted = true where e.entityRelationshiptypeId = :id")
	int deleteEntityRelaton(Long id);

	@Query(value = "SELECT entity_id1, entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id1 IN(?1) And er.deleted = false AND er.status =true ", nativeQuery = true)
	List<EntityRelationshipType> findByEntityId1AndDeleted(List<String> entityId);

	@Query(value = "SELECT entity_id1, entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id1 IN(?1) And er.deleted = false ", nativeQuery = true)
	List<EntityRelationshipType> findByEntityId1AndDeletedAndStatus(List<String> entityId);

	@Query(value = "SELECT entity_id2 entity_id1, entity_id1 entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id2 IN(?1) And er.deleted = false ", nativeQuery = true)
	List<EntityRelationshipType> findByEntityId2AndDeleted(List<String> entityId);

	@Query(value = "SELECT entity_id2 entity_id1, entity_id1 entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id1 IN(?1) And er.deleted = false ", nativeQuery = true)
	List<EntityRelationshipType> findByEntityId1AndDeletedREverse(List<String> entityId);

	@Query(value = "SELECT entity_id2 entity_id1, entity_id1 entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id2 IN(?1) AND er.deleted = false AND entity_id1 like '%C'", nativeQuery = true)
	List<EntityRelationshipType> findByEntityId2CAndEnityId1CInreverse(List<String> entityId);

	@Query(value = "SELECT entity_id2 entity_id1, entity_id1 entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id2 IN(?1) AND er.deleted = false AND entity_id1 like '%I'", nativeQuery = true)
	List<EntityRelationshipType> findByEntityId2CAndEnityId1IInReverse(List<String> entityId);

	@Query(value = "SELECT entity_id2 entity_id1, entity_id1 entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id1 IN(?1) AND er.deleted = false AND entity_id2 like '%C'", nativeQuery = true)
	List<EntityRelationshipType> findByEntityId1IAndReturnCListInreverse(List<String> entityId);

	@Query(value = "SELECT count(*) FROM enty.tb_entity_relationship_type_map WHERE entity_id1 = ?1 AND relationshiptype_id = ?2 AND deleted = FALSE ", nativeQuery = true)
	int findByEntityId1SpouseCount(String entityId1, Long relationshiptypeId);

	@Query(value = "SELECT count(*) FROM enty.tb_entity_relationship_type_map WHERE entity_id2 = ?1 AND relationshiptype_id = ?2 AND deleted = FALSE ", nativeQuery = true)
	int findByEntityId2SpouseCount(String entityId2, Long relationshiptypeId);

	@Query(value = "SELECT entity_id1, entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.relationshiptype_id = ?2 AND (er.entity_id1 = ?1 OR er.entity_id2 = ?1) AND er.status=true", nativeQuery = true)
	Optional<EntityRelationshipType> findByEntityId1IOrEntityId2(String entityId, Long relationshiptypeId);

}