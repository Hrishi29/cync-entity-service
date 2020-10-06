package com.idexcel.cync.los.entity.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.CommercialEntity;

@Repository
public interface CommercialEntityRepository extends JpaRepository<CommercialEntity, String> {

	Optional<CommercialEntity> findByBusinessNameIgnoreCase(String businessName);

	List<CommercialEntity> findByBusinessNameIgnoreCaseContaining(String entityName);

}
