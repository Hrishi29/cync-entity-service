package com.idexcel.cync.los.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.CommercialEntity;

@Repository
public interface CommercialNameSearchRepository  extends JpaRepository<CommercialEntity, String> {
	
	List<CommercialEntity> findByBusinessNameIgnoreCase(String businessName);

}
