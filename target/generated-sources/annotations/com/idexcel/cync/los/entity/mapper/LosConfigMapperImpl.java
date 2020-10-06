package com.idexcel.cync.los.entity.mapper;

import com.idexcel.cync.los.entity.dto.LosConfigDto;
import com.idexcel.cync.los.entity.model.LosConfig;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
@Component
public class LosConfigMapperImpl implements LosConfigMapper {

    @Override
    public LosConfigDto toLosConfigDto(LosConfig losConfig) {
        if ( losConfig == null ) {
            return null;
        }

        LosConfigDto losConfigDto = new LosConfigDto();

        losConfigDto.setConfigId( losConfig.getConfigId() );
        if ( losConfig.getClientId() != null ) {
            losConfigDto.setClientId( String.valueOf( losConfig.getClientId() ) );
        }
        if ( losConfig.getConfigtypeId() != null ) {
            losConfigDto.setConfigtypeId( losConfig.getConfigtypeId().intValue() );
        }
        losConfigDto.setConfigCode( losConfig.getConfigCode() );
        losConfigDto.setConfigValue( losConfig.getConfigValue() );

        return losConfigDto;
    }
}
