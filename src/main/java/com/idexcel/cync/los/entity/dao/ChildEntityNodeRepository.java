package com.idexcel.cync.los.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.ChildEntityNode;

@Repository
public interface ChildEntityNodeRepository extends JpaRepository<ChildEntityNode, Long> {

	@Query(value = "WITH RECURSIVE entity as (SELECT entity_id1, entity_id2, entity_relationshiptype_id, client_id, relationshiptype_id, ownership, status, deleted, created_by, created_at, modified_by, modified_at FROM enty.tb_entity_relationship_type_map er WHERE er.entity_id2 = ?1  And er.deleted =false "
			+ "UNION ALL SELECT er.entity_id1, er.entity_id2, er.entity_relationshiptype_id, er.client_id, er.relationshiptype_id, er.ownership, er.status, er.deleted,  er.created_by, er.created_at, er.modified_by, er.modified_at FROM enty.tb_entity_relationship_type_map er INNER JOIN entity et ON et.entity_id1 = er.entity_id2 AND et.deleted = false AND er.deleted=false) SELECT * FROM entity", nativeQuery = true)
	List<ChildEntityNode> findEntityTreeData(String entityId2);
	
	long countByParentIdAndActive(String entityId2, Boolean status);

	int countByParentIdAndDeleted(String entityId, Boolean status);
	
}
