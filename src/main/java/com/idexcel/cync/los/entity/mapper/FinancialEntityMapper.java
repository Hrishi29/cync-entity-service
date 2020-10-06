package com.idexcel.cync.los.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.idexcel.cync.los.entity.dto.CommercialEntityDto;
import com.idexcel.cync.los.entity.dto.FinancialEntityDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityDto;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;

/**
 * 
 * @author Amit
 *
 */
@Mapper
public interface FinancialEntityMapper {//NOSONAR
	FinancialEntityMapper INSTANCE = Mappers.getMapper(FinancialEntityMapper.class);

	@Mapping(target = "dateOfBirth", source = "financialEntity.dateOfBirth", dateFormat = "MM/dd/yyyy")
	IndividualEntityDto toIndividualEntityDto(IndividualFinancialEntity financialEntity);

	@Mapping(target = "businessType.businessTypeId", source = "businessType.configId")
	@Mapping(target = "businessType.businessCode", source = "businessType.configCode")
	@Mapping(target = "businessType.configTypeCode", source = "businessType.configtypeCode")
	@Mapping(target = "businessType.configValue", source = "businessType.configValue")
	@Mapping(target = "businessOpenDate", source = "financialEntity.businessOpenDate", dateFormat = "MM/dd/yyyy")
	@Mapping(target = "naics.naicsCodeId", source ="naics.configId" )
	@Mapping(target = "naics.naicsCode", source ="naics.configCode" )
	@Mapping(target = "naics.naicsCodeValue", source ="naics.configValue" )
	CommercialEntityDto toCommercialEntityDto(CommercialEntity financialEntity);

	@Mapping(target = "dateOfBirth", source = "financialEntityDto.dateOfBirth", dateFormat = "MM/dd/yyyy")
	IndividualFinancialEntity toIndividualFinancialEntity(IndividualEntityDto financialEntityDto);

	@Mapping(target = "businessOpenDate", source = "financialEntityDto.businessOpenDate", dateFormat = "MM/dd/yyyy")
	@Mapping(target = "businessType.configId", source = "businessType.businessTypeId")
	@Mapping(target = "businessType.configCode", source = "businessType.businessCode")
	@Mapping(target = "businessType.configtypeCode", source = "businessType.configTypeCode")
	@Mapping(target = "businessType.configValue", source = "businessType.configValue")
	@Mapping(target = "naics.configId", source = "naics.naicsCodeId")
	@Mapping(target = "naics.configCode", source = "naics.naicsCode")
	@Mapping(target = "naics.configValue", source = "naics.naicsCodeValue")
	CommercialEntity toCommercialEntity(CommercialEntityDto financialEntityDto);

	FinancialEntity toFinancialEntity(FinancialEntityDto financialEntityDto);

}
