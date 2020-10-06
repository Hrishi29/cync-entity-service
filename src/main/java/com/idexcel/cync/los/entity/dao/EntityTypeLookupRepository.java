package com.idexcel.cync.los.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.LosConfigDetails;

@Repository
public interface EntityTypeLookupRepository extends JpaRepository<LosConfigDetails, Long> {

}
