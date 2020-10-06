package com.idexcel.cync.los.entity.mapper;

import com.idexcel.cync.los.entity.dto.CommercialEntityDto;
import com.idexcel.cync.los.entity.dto.FinancialEntityDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityDto;
import com.idexcel.cync.los.entity.dto.LosBusinessTypeDto;
import com.idexcel.cync.los.entity.dto.NaicsDto;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
@Component
public class FinancialEntityMapperImpl implements FinancialEntityMapper {

    @Override
    public IndividualEntityDto toIndividualEntityDto(IndividualFinancialEntity financialEntity) {
        if ( financialEntity == null ) {
            return null;
        }

        IndividualEntityDto individualEntityDto = new IndividualEntityDto();

        if ( financialEntity.getDateOfBirth() != null ) {
            individualEntityDto.setDateOfBirth( new SimpleDateFormat( "MM/dd/yyyy" ).format( financialEntity.getDateOfBirth() ) );
        }
        individualEntityDto.setEntityId( financialEntity.getEntityId() );
        individualEntityDto.setEntityTypeConfig( financialEntity.getEntityTypeConfig() );
        individualEntityDto.setPhone( financialEntity.getPhone() );
        individualEntityDto.setEmail( financialEntity.getEmail() );
        individualEntityDto.setMap( financialEntity.getMap() );
        individualEntityDto.setNotes( financialEntity.getNotes() );
        individualEntityDto.setStatus( financialEntity.isStatus() );
        individualEntityDto.setRelationshipManagerEmail( financialEntity.getRelationshipManagerEmail() );
        individualEntityDto.setAddress( financialEntity.getAddress() );
        individualEntityDto.setCountry( financialEntity.getCountry() );
        individualEntityDto.setState( financialEntity.getState() );
        individualEntityDto.setCity( financialEntity.getCity() );
        individualEntityDto.setZipCode( financialEntity.getZipCode() );
        individualEntityDto.setAudit( financialEntity.getAudit() );
        individualEntityDto.setFirstName( financialEntity.getFirstName() );
        individualEntityDto.setMiddleName( financialEntity.getMiddleName() );
        individualEntityDto.setLastName( financialEntity.getLastName() );

        return individualEntityDto;
    }

    @Override
    public CommercialEntityDto toCommercialEntityDto(CommercialEntity financialEntity) {
        if ( financialEntity == null ) {
            return null;
        }

        CommercialEntityDto commercialEntityDto = new CommercialEntityDto();

        commercialEntityDto.setBusinessType( losConfigDetailsToLosBusinessTypeDto( financialEntity.getBusinessType() ) );
        commercialEntityDto.setNaics( losConfigDetailsToNaicsDto( financialEntity.getNaics() ) );
        if ( financialEntity.getBusinessOpenDate() != null ) {
            commercialEntityDto.setBusinessOpenDate( new SimpleDateFormat( "MM/dd/yyyy" ).format( financialEntity.getBusinessOpenDate() ) );
        }
        commercialEntityDto.setEntityId( financialEntity.getEntityId() );
        commercialEntityDto.setEntityTypeConfig( financialEntity.getEntityTypeConfig() );
        commercialEntityDto.setPhone( financialEntity.getPhone() );
        commercialEntityDto.setEmail( financialEntity.getEmail() );
        commercialEntityDto.setMap( financialEntity.getMap() );
        commercialEntityDto.setNotes( financialEntity.getNotes() );
        commercialEntityDto.setStatus( financialEntity.isStatus() );
        commercialEntityDto.setRelationshipManagerEmail( financialEntity.getRelationshipManagerEmail() );
        commercialEntityDto.setAddress( financialEntity.getAddress() );
        commercialEntityDto.setCountry( financialEntity.getCountry() );
        commercialEntityDto.setState( financialEntity.getState() );
        commercialEntityDto.setCity( financialEntity.getCity() );
        commercialEntityDto.setZipCode( financialEntity.getZipCode() );
        commercialEntityDto.setAudit( financialEntity.getAudit() );
        commercialEntityDto.setBusinessName( financialEntity.getBusinessName() );

        return commercialEntityDto;
    }

    @Override
    public IndividualFinancialEntity toIndividualFinancialEntity(IndividualEntityDto financialEntityDto) {
        if ( financialEntityDto == null ) {
            return null;
        }

        IndividualFinancialEntity individualFinancialEntity = new IndividualFinancialEntity();

        try {
            if ( financialEntityDto.getDateOfBirth() != null ) {
                individualFinancialEntity.setDateOfBirth( new SimpleDateFormat( "MM/dd/yyyy" ).parse( financialEntityDto.getDateOfBirth() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        individualFinancialEntity.setEntityId( financialEntityDto.getEntityId() );
        individualFinancialEntity.setEntityTypeConfig( financialEntityDto.getEntityTypeConfig() );
        individualFinancialEntity.setPhone( financialEntityDto.getPhone() );
        individualFinancialEntity.setEmail( financialEntityDto.getEmail() );
        individualFinancialEntity.setNotes( financialEntityDto.getNotes() );
        individualFinancialEntity.setStatus( financialEntityDto.isStatus() );
        individualFinancialEntity.setMap( financialEntityDto.getMap() );
        individualFinancialEntity.setCity( financialEntityDto.getCity() );
        individualFinancialEntity.setZipCode( financialEntityDto.getZipCode() );
        individualFinancialEntity.setCountry( financialEntityDto.getCountry() );
        individualFinancialEntity.setState( financialEntityDto.getState() );
        individualFinancialEntity.setRelationshipManagerEmail( financialEntityDto.getRelationshipManagerEmail() );
        individualFinancialEntity.setAddress( financialEntityDto.getAddress() );
        individualFinancialEntity.setAudit( financialEntityDto.getAudit() );
        individualFinancialEntity.setFirstName( financialEntityDto.getFirstName() );
        individualFinancialEntity.setMiddleName( financialEntityDto.getMiddleName() );
        individualFinancialEntity.setLastName( financialEntityDto.getLastName() );

        return individualFinancialEntity;
    }

    @Override
    public CommercialEntity toCommercialEntity(CommercialEntityDto financialEntityDto) {
        if ( financialEntityDto == null ) {
            return null;
        }

        CommercialEntity commercialEntity = new CommercialEntity();

        commercialEntity.setNaics( naicsDtoToLosConfigDetails( financialEntityDto.getNaics() ) );
        commercialEntity.setBusinessType( losBusinessTypeDtoToLosConfigDetails( financialEntityDto.getBusinessType() ) );
        try {
            if ( financialEntityDto.getBusinessOpenDate() != null ) {
                commercialEntity.setBusinessOpenDate( new SimpleDateFormat( "MM/dd/yyyy" ).parse( financialEntityDto.getBusinessOpenDate() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        commercialEntity.setEntityId( financialEntityDto.getEntityId() );
        commercialEntity.setEntityTypeConfig( financialEntityDto.getEntityTypeConfig() );
        commercialEntity.setPhone( financialEntityDto.getPhone() );
        commercialEntity.setEmail( financialEntityDto.getEmail() );
        commercialEntity.setNotes( financialEntityDto.getNotes() );
        commercialEntity.setStatus( financialEntityDto.isStatus() );
        commercialEntity.setMap( financialEntityDto.getMap() );
        commercialEntity.setCity( financialEntityDto.getCity() );
        commercialEntity.setZipCode( financialEntityDto.getZipCode() );
        commercialEntity.setCountry( financialEntityDto.getCountry() );
        commercialEntity.setState( financialEntityDto.getState() );
        commercialEntity.setRelationshipManagerEmail( financialEntityDto.getRelationshipManagerEmail() );
        commercialEntity.setAddress( financialEntityDto.getAddress() );
        commercialEntity.setAudit( financialEntityDto.getAudit() );
        commercialEntity.setBusinessName( financialEntityDto.getBusinessName() );

        return commercialEntity;
    }

    @Override
    public FinancialEntity toFinancialEntity(FinancialEntityDto financialEntityDto) {
        if ( financialEntityDto == null ) {
            return null;
        }

        FinancialEntity financialEntity = new FinancialEntity();

        financialEntity.setEntityId( financialEntityDto.getEntityId() );
        financialEntity.setEntityTypeConfig( financialEntityDto.getEntityTypeConfig() );
        financialEntity.setPhone( financialEntityDto.getPhone() );
        financialEntity.setEmail( financialEntityDto.getEmail() );
        financialEntity.setNotes( financialEntityDto.getNotes() );
        financialEntity.setStatus( financialEntityDto.isStatus() );
        financialEntity.setMap( financialEntityDto.getMap() );
        financialEntity.setCity( financialEntityDto.getCity() );
        financialEntity.setZipCode( financialEntityDto.getZipCode() );
        financialEntity.setCountry( financialEntityDto.getCountry() );
        financialEntity.setState( financialEntityDto.getState() );
        financialEntity.setRelationshipManagerEmail( financialEntityDto.getRelationshipManagerEmail() );
        financialEntity.setAddress( financialEntityDto.getAddress() );
        financialEntity.setAudit( financialEntityDto.getAudit() );

        return financialEntity;
    }

    protected LosBusinessTypeDto losConfigDetailsToLosBusinessTypeDto(LosConfigDetails losConfigDetails) {
        if ( losConfigDetails == null ) {
            return null;
        }

        LosBusinessTypeDto losBusinessTypeDto = new LosBusinessTypeDto();

        losBusinessTypeDto.setBusinessCode( losConfigDetails.getConfigCode() );
        losBusinessTypeDto.setConfigValue( losConfigDetails.getConfigValue() );
        losBusinessTypeDto.setConfigTypeCode( losConfigDetails.getConfigtypeCode() );
        losBusinessTypeDto.setBusinessTypeId( losConfigDetails.getConfigId() );

        return losBusinessTypeDto;
    }

    protected NaicsDto losConfigDetailsToNaicsDto(LosConfigDetails losConfigDetails) {
        if ( losConfigDetails == null ) {
            return null;
        }

        NaicsDto naicsDto = new NaicsDto();

        naicsDto.setNaicsCode( losConfigDetails.getConfigCode() );
        naicsDto.setNaicsCodeValue( losConfigDetails.getConfigValue() );
        naicsDto.setNaicsCodeId( losConfigDetails.getConfigId() );

        return naicsDto;
    }

    protected LosConfigDetails naicsDtoToLosConfigDetails(NaicsDto naicsDto) {
        if ( naicsDto == null ) {
            return null;
        }

        LosConfigDetails losConfigDetails = new LosConfigDetails();

        losConfigDetails.setConfigId( naicsDto.getNaicsCodeId() );
        losConfigDetails.setConfigCode( naicsDto.getNaicsCode() );
        losConfigDetails.setConfigValue( naicsDto.getNaicsCodeValue() );

        return losConfigDetails;
    }

    protected LosConfigDetails losBusinessTypeDtoToLosConfigDetails(LosBusinessTypeDto losBusinessTypeDto) {
        if ( losBusinessTypeDto == null ) {
            return null;
        }

        LosConfigDetails losConfigDetails = new LosConfigDetails();

        losConfigDetails.setConfigtypeCode( losBusinessTypeDto.getConfigTypeCode() );
        losConfigDetails.setConfigValue( losBusinessTypeDto.getConfigValue() );
        losConfigDetails.setConfigId( losBusinessTypeDto.getBusinessTypeId() );
        losConfigDetails.setConfigCode( losBusinessTypeDto.getBusinessCode() );

        return losConfigDetails;
    }
}
