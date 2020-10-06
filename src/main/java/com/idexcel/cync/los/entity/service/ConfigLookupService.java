package com.idexcel.cync.los.entity.service;

import java.util.List;

import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.LosConfigType;

public interface ConfigLookupService {

	public List<LosConfigDetails> findConfigDetails(String configtypeCode);
	
	public List<LosConfigType> findAllConfigTypes();

}
