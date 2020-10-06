package com.idexcel.cync.los.entity.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfigDetails;

@Repository
public interface FinancialRepository extends JpaRepository<FinancialEntity, String> {

	@Modifying
	@Transactional
	@Query("update FinancialEntity e set e.status = false where e.entityId = :entityId")
	int deactivateEntity(String entityId);

	@Modifying
	@Transactional
	@Query("update FinancialEntity e set e.status = true where e.entityId = :entityId")
	int activateEntity(String entityId);

	List<FinancialEntity> findByEntityTypeConfig(Optional<LosConfigDetails> entityTypeDetails, Sort sort);

}
