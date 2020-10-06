package com.idexcel.cync.los.entity.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.StateLookup;

@Repository
public interface StateLookupRepository extends JpaRepository<StateLookup, Long> {

	List<StateLookup> findByCountryId(Long countryId, Sort sort);

	Optional<StateLookup> findByStateId(Long stateId);

	StateLookup findByStateIdAndCountryId(Long stateId, Long countryId);
}
