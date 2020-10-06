package com.idexcel.cync.los.entity.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.EntityNodeSpouse;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;

@Repository
public interface IndividualEntityRepository extends JpaRepository<IndividualFinancialEntity, String> {

	List<IndividualFinancialEntity> findByFirstNameIgnoreCaseContainingOrMiddleNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
			String firstName, String middleName, String lastName);

	Optional<IndividualFinancialEntity> findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(
			String firstName, String middleName, String lastName);

	List<IndividualFinancialEntity> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
			String splittedFName, String splittedLName);

	List<IndividualFinancialEntity> findByFirstNameIgnoreCaseContaining(String splittedFName);

	List<IndividualFinancialEntity> findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
			String splittedFName, String splittedMName, String splittedLName);

	List<IndividualFinancialEntity> findByFirstNameIgnoreCaseContainingAndMiddleNameIgnoreCaseContaining(
			String splittedFName, String splittedMName);

	List<IndividualFinancialEntity> findByLastNameIgnoreCaseContaining(String splittedFName);

	@Query(value = "select entity_id,first_name,last_name from enty.tb_individual_entity_details where entity_id = ?1", nativeQuery = true)
	Optional<EntityNodeSpouse> getSpouse(String entityId);

}
