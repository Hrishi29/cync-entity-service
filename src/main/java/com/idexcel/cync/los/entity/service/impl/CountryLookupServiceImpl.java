package com.idexcel.cync.los.entity.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.dao.CountryLookupRepository;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.service.CountryLookupService;

@Service
public class CountryLookupServiceImpl implements CountryLookupService {

	private static final Logger LOG = LoggerFactory.getLogger(CountryLookupServiceImpl.class);
	private CountryLookupRepository countryLookupRepository;

	@Autowired
	public CountryLookupServiceImpl(CountryLookupRepository countryLookupRepository) {
		this.countryLookupRepository = countryLookupRepository;
	}

	/**
	 * Method to Find the List of Countries
	 */
	public List<CountryLookup> findCountryList() {
		LOG.debug("fetching country list...");
		return countryLookupRepository.findAll(Sort.by(Sort.Order.asc("countryName")));
	}
}
