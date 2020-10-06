package com.idexcel.cync.los.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.CountryLookup;

@Repository
public interface CountryLookupRepository extends JpaRepository<CountryLookup, Long> {

	CountryLookup findByCountryId(Long countryId);

}
