package com.idexcel.cync.los.entity.mapper;

import com.idexcel.cync.los.entity.dto.CommercialEntityListDto;
import com.idexcel.cync.los.entity.dto.FinancialEntityListDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityListDto;
import com.idexcel.cync.los.entity.model.CommercialEntityList;
import com.idexcel.cync.los.entity.model.FinancialEntityList;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntityList;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
@Component
public class FinancialEntityListMapperImpl implements FinancialEntityListMapper {

    @Override
    public IndividualEntityListDto toIndividualEntityListDto(IndividualFinancialEntityList financialEntity) {
        if ( financialEntity == null ) {
            return null;
        }

        IndividualEntityListDto individualEntityListDto = new IndividualEntityListDto();

        individualEntityListDto.setEntityId( financialEntity.getEntityId() );
        individualEntityListDto.setEntityTypeConfig( financialEntity.getEntityTypeConfig() );
        individualEntityListDto.setStatus( financialEntity.isStatus() );
        individualEntityListDto.setRelationshipManagerEmail( financialEntity.getRelationshipManagerEmail() );
        individualEntityListDto.setAudit( financialEntity.getAudit() );
        individualEntityListDto.setFirstName( financialEntity.getFirstName() );
        individualEntityListDto.setMiddleName( financialEntity.getMiddleName() );
        individualEntityListDto.setLastName( financialEntity.getLastName() );

        return individualEntityListDto;
    }

    @Override
    public CommercialEntityListDto toCommercialEntityListDto(CommercialEntityList financialEntity) {
        if ( financialEntity == null ) {
            return null;
        }

        CommercialEntityListDto commercialEntityListDto = new CommercialEntityListDto();

        commercialEntityListDto.setEntityId( financialEntity.getEntityId() );
        commercialEntityListDto.setEntityTypeConfig( financialEntity.getEntityTypeConfig() );
        commercialEntityListDto.setStatus( financialEntity.isStatus() );
        commercialEntityListDto.setRelationshipManagerEmail( financialEntity.getRelationshipManagerEmail() );
        commercialEntityListDto.setAudit( financialEntity.getAudit() );
        commercialEntityListDto.setBusinessName( financialEntity.getBusinessName() );

        return commercialEntityListDto;
    }

    @Override
    public IndividualFinancialEntityList toIndividualFinancialEntity(IndividualEntityListDto financialEntityDto) {
        if ( financialEntityDto == null ) {
            return null;
        }

        IndividualFinancialEntityList individualFinancialEntityList = new IndividualFinancialEntityList();

        individualFinancialEntityList.setEntityId( financialEntityDto.getEntityId() );
        individualFinancialEntityList.setEntityTypeConfig( financialEntityDto.getEntityTypeConfig() );
        individualFinancialEntityList.setStatus( financialEntityDto.isStatus() );
        individualFinancialEntityList.setRelationshipManagerEmail( financialEntityDto.getRelationshipManagerEmail() );
        individualFinancialEntityList.setAudit( financialEntityDto.getAudit() );
        individualFinancialEntityList.setFirstName( financialEntityDto.getFirstName() );
        individualFinancialEntityList.setMiddleName( financialEntityDto.getMiddleName() );
        individualFinancialEntityList.setLastName( financialEntityDto.getLastName() );

        return individualFinancialEntityList;
    }

    @Override
    public CommercialEntityList toCommercialEntity(CommercialEntityListDto financialEntityDto) {
        if ( financialEntityDto == null ) {
            return null;
        }

        CommercialEntityList commercialEntityList = new CommercialEntityList();

        commercialEntityList.setEntityId( financialEntityDto.getEntityId() );
        commercialEntityList.setEntityTypeConfig( financialEntityDto.getEntityTypeConfig() );
        commercialEntityList.setStatus( financialEntityDto.isStatus() );
        commercialEntityList.setRelationshipManagerEmail( financialEntityDto.getRelationshipManagerEmail() );
        commercialEntityList.setAudit( financialEntityDto.getAudit() );
        commercialEntityList.setBusinessName( financialEntityDto.getBusinessName() );

        return commercialEntityList;
    }

    @Override
    public FinancialEntityList toFinancialEntityList(FinancialEntityListDto financialEntityDto) {
        if ( financialEntityDto == null ) {
            return null;
        }

        FinancialEntityList financialEntityList = new FinancialEntityList();

        financialEntityList.setEntityId( financialEntityDto.getEntityId() );
        financialEntityList.setEntityTypeConfig( financialEntityDto.getEntityTypeConfig() );
        financialEntityList.setStatus( financialEntityDto.isStatus() );
        financialEntityList.setRelationshipManagerEmail( financialEntityDto.getRelationshipManagerEmail() );
        financialEntityList.setAudit( financialEntityDto.getAudit() );

        return financialEntityList;
    }

    @Override
    public List<FinancialEntityList> toFinancialEntityLists(List<FinancialEntityListDto> financialEntityDtoList) {
        if ( financialEntityDtoList == null ) {
            return null;
        }

        List<FinancialEntityList> list = new ArrayList<FinancialEntityList>( financialEntityDtoList.size() );
        for ( FinancialEntityListDto financialEntityListDto : financialEntityDtoList ) {
            list.add( toFinancialEntityList( financialEntityListDto ) );
        }

        return list;
    }

    @Override
    public FinancialEntityListDto toFinancialEntityListDto(FinancialEntityList financialEntityDto) {
        if ( financialEntityDto == null ) {
            return null;
        }

        FinancialEntityListDto financialEntityListDto = new FinancialEntityListDto();

        financialEntityListDto.setEntityId( financialEntityDto.getEntityId() );
        financialEntityListDto.setEntityTypeConfig( financialEntityDto.getEntityTypeConfig() );
        financialEntityListDto.setStatus( financialEntityDto.isStatus() );
        financialEntityListDto.setRelationshipManagerEmail( financialEntityDto.getRelationshipManagerEmail() );
        financialEntityListDto.setAudit( financialEntityDto.getAudit() );

        return financialEntityListDto;
    }

    @Override
    public List<FinancialEntityListDto> toFinancialEntityListsDto(List<FinancialEntityList> financialEntityDtoList) {
        if ( financialEntityDtoList == null ) {
            return null;
        }

        List<FinancialEntityListDto> list = new ArrayList<FinancialEntityListDto>( financialEntityDtoList.size() );
        for ( FinancialEntityList financialEntityList : financialEntityDtoList ) {
            list.add( toFinancialEntityListDto( financialEntityList ) );
        }

        return list;
    }
}
