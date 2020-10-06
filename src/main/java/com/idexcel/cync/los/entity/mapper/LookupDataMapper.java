package com.idexcel.cync.los.entity.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.idexcel.cync.los.entity.dto.CountryLookupDto;
import com.idexcel.cync.los.entity.dto.CountryStateLookupDto;
import com.idexcel.cync.los.entity.dto.LosConfigDetailsDto;
import com.idexcel.cync.los.entity.dto.LosConfigTypeDto;
import com.idexcel.cync.los.entity.dto.StateLookupDto;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.LosConfigType;
import com.idexcel.cync.los.entity.model.StateLookup;

/**
 * 
 * @author Amit
 *
 */

@Mapper
public interface LookupDataMapper {// NOSONAR
	LookupDataMapper INSTANCE = Mappers.getMapper(LookupDataMapper.class);

	CountryLookupDto toCountryLookupDto(CountryLookup countryLookup);

	StateLookupDto toStateLookupDto(StateLookup countryLookup);

	@Mapping(target = "countryName", source = "countryLookup.countryName")
	@Mapping(target = "countryCode", source = "countryLookup.countryCode")
	CountryStateLookupDto toCountryStateLookupDto(StateLookup countryLookup);

	List<CountryLookupDto> convertToListCountryLookupDto(List<CountryLookup> countryLookup);

	List<StateLookupDto> convertToListStateLookupDto(List<StateLookup> stateLookup);

	LosConfigDetailsDto toConfigDetailsDto(LosConfigDetails losConfigDetails);

	List<LosConfigDetailsDto> toConfigDetailsDto(List<LosConfigDetails> losConfigDetails);

	List<LosConfigTypeDto> toLosConfigTypeDto(List<LosConfigType> configTypeList);
}
