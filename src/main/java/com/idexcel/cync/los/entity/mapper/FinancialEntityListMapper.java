package com.idexcel.cync.los.entity.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.idexcel.cync.los.entity.dto.CommercialEntityListDto;
import com.idexcel.cync.los.entity.dto.FinancialEntityListDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityListDto;
import com.idexcel.cync.los.entity.model.CommercialEntityList;
import com.idexcel.cync.los.entity.model.FinancialEntityList;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntityList;

/**
 * 
 * @author
 *
 */
@Mapper
public interface FinancialEntityListMapper {// NOSONAR
	FinancialEntityListMapper INSTANCE = Mappers.getMapper(FinancialEntityListMapper.class);

	IndividualEntityListDto toIndividualEntityListDto(IndividualFinancialEntityList financialEntity);

	CommercialEntityListDto toCommercialEntityListDto(CommercialEntityList financialEntity);

	IndividualFinancialEntityList toIndividualFinancialEntity(IndividualEntityListDto financialEntityDto);

	CommercialEntityList toCommercialEntity(CommercialEntityListDto financialEntityDto);

	FinancialEntityList toFinancialEntityList(FinancialEntityListDto financialEntityDto);
	
	List<FinancialEntityList> toFinancialEntityLists(List<FinancialEntityListDto> financialEntityDtoList);
	
	FinancialEntityListDto toFinancialEntityListDto(FinancialEntityList financialEntityDto);
	
	List<FinancialEntityListDto> toFinancialEntityListsDto(List<FinancialEntityList> financialEntityDtoList);
	
	
	

}
