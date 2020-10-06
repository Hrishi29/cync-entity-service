package com.idexcel.cync.los.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.idexcel.cync.los.entity.dto.LosConfigDto;
import com.idexcel.cync.los.entity.model.LosConfig;

/**
 * 
 * @author Amit
 *
 */

@Mapper
public interface LosConfigMapper {// NOSONAR
	LosConfigMapper INSTANCE = Mappers.getMapper(LosConfigMapper.class);

	LosConfigDto toLosConfigDto(LosConfig losConfig);

}
