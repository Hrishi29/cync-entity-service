package com.idexcel.cync.los.entity.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.dao.StateLookupRepository;
import com.idexcel.cync.los.entity.model.StateLookup;
import com.idexcel.cync.los.entity.service.StateLookupService;
import com.idexcel.cync.los.entity.validator.FSCountryStateValidator;

@Service
public class StateLookupServiceImpl implements StateLookupService {

	private static final Logger LOG = LoggerFactory.getLogger(StateLookupServiceImpl.class);
	private StateLookupRepository stateLookupRepository;
	private FSCountryStateValidator fSCountryStateValidator;

	@Autowired
	public StateLookupServiceImpl(StateLookupRepository stateLookupRepository,
			FSCountryStateValidator fSCountryStateValidator) {
		this.stateLookupRepository = stateLookupRepository;
		this.fSCountryStateValidator = fSCountryStateValidator;
	}

	/**
	 * Method to find StateList BasedOn countryId
	 */
	@Override
	public List<StateLookup> findStateList(Long countryId) {
		LOG.debug("fetching state list for country...");
		Sort sort = Sort.by(Sort.Order.asc("stateName"));
		fSCountryStateValidator.isValidCountryId(countryId);
		return stateLookupRepository.findByCountryId(countryId, sort);
	}

	/**
	 * Method to find All States and return State List
	 */
	@Override
	public List<StateLookup> stateList() {
		Sort sort = Sort.by(Sort.Order.asc("stateName"));
		LOG.debug("fetching State list ...");
		return stateLookupRepository.findAll(sort);
	}
}
