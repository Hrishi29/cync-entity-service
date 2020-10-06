package com.idexcel.cync.los.entity.service.impl;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_CONFIG_TYPE_CODE;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.dao.ConfigLookupRepository;
import com.idexcel.cync.los.entity.dao.LosConfigTypeRepository;
import com.idexcel.cync.los.entity.exception.ResourceNotFoundException;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.LosConfigType;
import com.idexcel.cync.los.entity.service.ConfigLookupService;

@Service
public class ConfigLookupServiceImpl implements ConfigLookupService {

	private ConfigLookupRepository configLookupRepository;
	private LosConfigTypeRepository losConfigTypeRepository;
	private final MessageSource errorMsgs;

	@Autowired
	public ConfigLookupServiceImpl(ConfigLookupRepository configLookupRepository,
			LosConfigTypeRepository losConfigTypeRepository, MessageSource errorMsgs) {
		this.configLookupRepository = configLookupRepository;
		this.losConfigTypeRepository = losConfigTypeRepository;
		this.errorMsgs = errorMsgs;
	}

	/**
	 * Method to Find the List of LosConfigDetails Based on configtypeCode
	 */
	@Override
	public List<LosConfigDetails> findConfigDetails(String configtypeCode) {
		String configtypeCodetoUppdercase = configtypeCode.toUpperCase().replace("-", "_").trim();
		List<LosConfigType> configTypeCode = losConfigTypeRepository.findByConfigtypeCode(configtypeCodetoUppdercase);
		List<LosConfigType> ctype = configTypeCode.stream()
				.filter(x -> x.getConfigtypeCode().equals(LOSEntityConstants.COUNTRY)
						|| x.getConfigtypeCode().equals(LOSEntityConstants.STATE))
				.collect(Collectors.toList());
		configTypeCode.removeAll(ctype);
		if (configTypeCode.isEmpty()) {
			throw new ResourceNotFoundException(errorMsgs.getMessage(INVALID_CONFIG_TYPE_CODE,
					new Object[] { configtypeCode }, CommonUtils.getLocale()));
		}
		return configLookupRepository.findByConfigtypeCode(configtypeCodetoUppdercase);
	}

	/**
	 * Method to Find the List of LosConfigType
	 */
	@Override
	public List<LosConfigType> findAllConfigTypes() {
		List<LosConfigType> losConfigType = losConfigTypeRepository.findAll();
		List<LosConfigType> ctype = losConfigType.stream()
				.filter(x -> x.getConfigtypeCode().equals(LOSEntityConstants.COUNTRY)
						|| x.getConfigtypeCode().equals(LOSEntityConstants.STATE))
				.collect(Collectors.toList());
		losConfigType.removeAll(ctype);
		return losConfigType;
	}
}
