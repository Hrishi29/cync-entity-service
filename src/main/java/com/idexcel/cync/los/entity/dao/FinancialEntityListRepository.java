package com.idexcel.cync.los.entity.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.FinancialEntityList;
import com.idexcel.cync.los.entity.model.LosConfigDetails;

@Repository
public interface FinancialEntityListRepository extends JpaRepository<FinancialEntityList, String> {

	List<FinancialEntityList> findByEntityTypeConfig(Optional<LosConfigDetails> entityTypeDetails, Sort sort);

}
