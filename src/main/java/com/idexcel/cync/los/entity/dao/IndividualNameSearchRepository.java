package com.idexcel.cync.los.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;

@Repository
public interface IndividualNameSearchRepository extends JpaRepository<IndividualFinancialEntity, String> {
	
	List<IndividualFinancialEntity> findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(
			String firstName, String middleName, String lastName);

}
