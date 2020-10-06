package com.idexcel.cync.los.entity.mapper;

import com.idexcel.cync.los.entity.dto.CountryLookupDto;
import com.idexcel.cync.los.entity.dto.CountryStateLookupDto;
import com.idexcel.cync.los.entity.dto.LosConfigDetailsDto;
import com.idexcel.cync.los.entity.dto.LosConfigTypeDto;
import com.idexcel.cync.los.entity.dto.StateLookupDto;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.LosConfigType;
import com.idexcel.cync.los.entity.model.StateLookup;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
@Component
public class LookupDataMapperImpl implements LookupDataMapper {

    @Override
    public CountryLookupDto toCountryLookupDto(CountryLookup countryLookup) {
        if ( countryLookup == null ) {
            return null;
        }

        CountryLookupDto countryLookupDto = new CountryLookupDto();

        countryLookupDto.setCountryId( countryLookup.getCountryId() );
        countryLookupDto.setCountryCode( countryLookup.getCountryCode() );
        countryLookupDto.setCountryName( countryLookup.getCountryName() );

        return countryLookupDto;
    }

    @Override
    public StateLookupDto toStateLookupDto(StateLookup countryLookup) {
        if ( countryLookup == null ) {
            return null;
        }

        StateLookupDto stateLookupDto = new StateLookupDto();

        stateLookupDto.setCountryId( countryLookup.getCountryId() );
        stateLookupDto.setStateId( countryLookup.getStateId() );
        stateLookupDto.setStateCode( countryLookup.getStateCode() );
        stateLookupDto.setStateName( countryLookup.getStateName() );

        return stateLookupDto;
    }

    @Override
    public CountryStateLookupDto toCountryStateLookupDto(StateLookup countryLookup) {
        if ( countryLookup == null ) {
            return null;
        }

        CountryStateLookupDto countryStateLookupDto = new CountryStateLookupDto();

        countryStateLookupDto.setCountryCode( countryLookupCountryLookupCountryCode( countryLookup ) );
        countryStateLookupDto.setCountryName( countryLookupCountryLookupCountryName( countryLookup ) );
        countryStateLookupDto.setCountryId( countryLookup.getCountryId() );
        countryStateLookupDto.setStateId( countryLookup.getStateId() );
        countryStateLookupDto.setStateCode( countryLookup.getStateCode() );
        countryStateLookupDto.setStateName( countryLookup.getStateName() );

        return countryStateLookupDto;
    }

    @Override
    public List<CountryLookupDto> convertToListCountryLookupDto(List<CountryLookup> countryLookup) {
        if ( countryLookup == null ) {
            return null;
        }

        List<CountryLookupDto> list = new ArrayList<CountryLookupDto>( countryLookup.size() );
        for ( CountryLookup countryLookup1 : countryLookup ) {
            list.add( toCountryLookupDto( countryLookup1 ) );
        }

        return list;
    }

    @Override
    public List<StateLookupDto> convertToListStateLookupDto(List<StateLookup> stateLookup) {
        if ( stateLookup == null ) {
            return null;
        }

        List<StateLookupDto> list = new ArrayList<StateLookupDto>( stateLookup.size() );
        for ( StateLookup stateLookup1 : stateLookup ) {
            list.add( toStateLookupDto( stateLookup1 ) );
        }

        return list;
    }

    @Override
    public LosConfigDetailsDto toConfigDetailsDto(LosConfigDetails losConfigDetails) {
        if ( losConfigDetails == null ) {
            return null;
        }

        LosConfigDetailsDto losConfigDetailsDto = new LosConfigDetailsDto();

        losConfigDetailsDto.setConfigId( losConfigDetails.getConfigId() );
        losConfigDetailsDto.setConfigCode( losConfigDetails.getConfigCode() );
        losConfigDetailsDto.setConfigtypeId( losConfigDetails.getConfigtypeId() );
        losConfigDetailsDto.setConfigtypeCode( losConfigDetails.getConfigtypeCode() );
        losConfigDetailsDto.setConfigValue( losConfigDetails.getConfigValue() );

        return losConfigDetailsDto;
    }

    @Override
    public List<LosConfigDetailsDto> toConfigDetailsDto(List<LosConfigDetails> losConfigDetails) {
        if ( losConfigDetails == null ) {
            return null;
        }

        List<LosConfigDetailsDto> list = new ArrayList<LosConfigDetailsDto>( losConfigDetails.size() );
        for ( LosConfigDetails losConfigDetails1 : losConfigDetails ) {
            list.add( toConfigDetailsDto( losConfigDetails1 ) );
        }

        return list;
    }

    @Override
    public List<LosConfigTypeDto> toLosConfigTypeDto(List<LosConfigType> configTypeList) {
        if ( configTypeList == null ) {
            return null;
        }

        List<LosConfigTypeDto> list = new ArrayList<LosConfigTypeDto>( configTypeList.size() );
        for ( LosConfigType losConfigType : configTypeList ) {
            list.add( losConfigTypeToLosConfigTypeDto( losConfigType ) );
        }

        return list;
    }

    private String countryLookupCountryLookupCountryCode(StateLookup stateLookup) {
        if ( stateLookup == null ) {
            return null;
        }
        CountryLookup countryLookup = stateLookup.getCountryLookup();
        if ( countryLookup == null ) {
            return null;
        }
        String countryCode = countryLookup.getCountryCode();
        if ( countryCode == null ) {
            return null;
        }
        return countryCode;
    }

    private String countryLookupCountryLookupCountryName(StateLookup stateLookup) {
        if ( stateLookup == null ) {
            return null;
        }
        CountryLookup countryLookup = stateLookup.getCountryLookup();
        if ( countryLookup == null ) {
            return null;
        }
        String countryName = countryLookup.getCountryName();
        if ( countryName == null ) {
            return null;
        }
        return countryName;
    }

    protected LosConfigTypeDto losConfigTypeToLosConfigTypeDto(LosConfigType losConfigType) {
        if ( losConfigType == null ) {
            return null;
        }

        LosConfigTypeDto losConfigTypeDto = new LosConfigTypeDto();

        losConfigTypeDto.setConfigTypeId( losConfigType.getConfigTypeId() );
        losConfigTypeDto.setClientId( losConfigType.getClientId() );
        losConfigTypeDto.setConfigtypeCode( losConfigType.getConfigtypeCode() );
        losConfigTypeDto.setConfigtypeName( losConfigType.getConfigtypeName() );

        return losConfigTypeDto;
    }
}
