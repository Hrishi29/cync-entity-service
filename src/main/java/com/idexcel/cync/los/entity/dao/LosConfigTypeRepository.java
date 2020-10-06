package com.idexcel.cync.los.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.LosConfigType;

@Repository
public interface LosConfigTypeRepository extends JpaRepository<LosConfigType, Long> {

	List<LosConfigType> findByConfigtypeCode(String configtypeCode);

}