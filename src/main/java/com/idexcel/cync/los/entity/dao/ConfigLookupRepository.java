package com.idexcel.cync.los.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.LosConfigDetails;

@Repository
public interface ConfigLookupRepository extends JpaRepository<LosConfigDetails, Long> {
	
	LosConfigDetails findByConfigId(Long configId);

	List<LosConfigDetails> findByConfigtypeCode(String string);

}
