package com.idexcel.cync.los.entity.service;

import java.util.List;

import com.idexcel.cync.los.entity.model.StateLookup;

public interface StateLookupService {

	public List<StateLookup> findStateList(Long countryId);

	public List<StateLookup> stateList();

}
