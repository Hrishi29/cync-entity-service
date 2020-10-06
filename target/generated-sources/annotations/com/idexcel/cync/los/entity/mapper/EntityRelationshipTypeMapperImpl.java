package com.idexcel.cync.los.entity.mapper;

import com.idexcel.cync.los.entity.dto.EntityNodeDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipBorrowerDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipTypeDto;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.ParentEntityNode;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
@Component
public class EntityRelationshipTypeMapperImpl implements EntityRelationshipTypeMapper {

    @Autowired
    private LookupDataMapper lookupDataMapper;

    @Override
    public EntityRelationshipType toEntityRelationshipType(EntityRelationshipTypeDto entityRelationshipTypeDto) {
        if ( entityRelationshipTypeDto == null ) {
            return null;
        }

        EntityRelationshipType entityRelationshipType = new EntityRelationshipType();

        entityRelationshipType.setRelationshiptypeId( entityRelationshipTypeDtoEntityRelationConfigDetailConfigId( entityRelationshipTypeDto ) );
        entityRelationshipType.setEntityRelationshiptypeId( entityRelationshipTypeDto.getEntityRelationshiptypeId() );
        entityRelationshipType.setEntityId1( entityRelationshipTypeDto.getEntityId1() );
        entityRelationshipType.setParentEntity( entityRelationshipTypeDto.getParentEntity() );
        entityRelationshipType.setEntityId2( entityRelationshipTypeDto.getEntityId2() );
        entityRelationshipType.setChildEntity( entityRelationshipTypeDto.getChildEntity() );
        entityRelationshipType.setEntityRelationConfigDetail( entityRelationshipTypeDto.getEntityRelationConfigDetail() );
        entityRelationshipType.setOwnership( entityRelationshipTypeDto.getOwnership() );
        entityRelationshipType.setStatus( entityRelationshipTypeDto.isStatus() );

        return entityRelationshipType;
    }

    @Override
    public EntityRelationshipType toEntityRelationshipCreteType(EntityRelationshipDto entityRelationshipDto) {
        if ( entityRelationshipDto == null ) {
            return null;
        }

        EntityRelationshipType entityRelationshipType = new EntityRelationshipType();

        entityRelationshipType.setRelationshiptypeId( entityRelationshipDtoEntityRelationConfigDetailConfigId( entityRelationshipDto ) );
        entityRelationshipType.setEntityId2( entityRelationshipDto.getEntityId2() );
        entityRelationshipType.setEntityRelationConfigDetail( entityRelationshipDto.getEntityRelationConfigDetail() );
        entityRelationshipType.setOwnership( entityRelationshipDto.getOwnership() );
        entityRelationshipType.setStatus( entityRelationshipDto.isStatus() );

        return entityRelationshipType;
    }

    @Override
    public EntityNodeDto toEntityNodeDto(ParentEntityNode parentEntityNode) {
        if ( parentEntityNode == null ) {
            return null;
        }

        EntityNodeDto entityNodeDto = new EntityNodeDto();

        entityNodeDto.setLosConfigDetails( lookupDataMapper.toConfigDetailsDto( parentEntityNode.getLosConfigDetails() ) );
        entityNodeDto.setChildren( parentEntityNodeListToEntityNodeDtoList( parentEntityNode.getChildren() ) );
        entityNodeDto.setParentEntity( parentEntityNode.getParentEntity() );
        entityNodeDto.setChildEntity( parentEntityNode.getChildEntity() );
        entityNodeDto.setChildId( parentEntityNode.getChildId() );
        entityNodeDto.setParentId( parentEntityNode.getParentId() );

        return entityNodeDto;
    }

    @Override
    public List<EntityRelationshipTypeDto> toEntityRelationshipTypeDto(List<EntityRelationshipType> entityRelationshipType) {
        if ( entityRelationshipType == null ) {
            return null;
        }

        List<EntityRelationshipTypeDto> list = new ArrayList<EntityRelationshipTypeDto>( entityRelationshipType.size() );
        for ( EntityRelationshipType entityRelationshipType1 : entityRelationshipType ) {
            list.add( entityRelationshipTypeToEntityRelationshipTypeDto( entityRelationshipType1 ) );
        }

        return list;
    }

    @Override
    public EntityRelationshipBorrowerDto toEntityRelationshipBorrowerDto(EntityRelationshipType entityRelationshipType) {
        if ( entityRelationshipType == null ) {
            return null;
        }

        EntityRelationshipBorrowerDto entityRelationshipBorrowerDto = new EntityRelationshipBorrowerDto();

        getEntityType( entityRelationshipBorrowerDto, entityRelationshipType );

        entityRelationshipBorrowerDto.setEntityId( entityRelationshipTypeChildEntityEntityId( entityRelationshipType ) );
        entityRelationshipBorrowerDto.setEntityType( entityRelationshipTypeChildEntityEntityTypeConfigConfigCode( entityRelationshipType ) );
        entityRelationshipBorrowerDto.setRelation( entityRelationshipTypeEntityRelationConfigDetailConfigCode( entityRelationshipType ) );
        entityRelationshipBorrowerDto.setOwnership( entityRelationshipType.getOwnership() );

        return entityRelationshipBorrowerDto;
    }

    @Override
    public List<EntityRelationshipBorrowerDto> toEntityRelationshipBorrowerDto(List<EntityRelationshipType> entityRelationshipType) {
        if ( entityRelationshipType == null ) {
            return null;
        }

        List<EntityRelationshipBorrowerDto> list = new ArrayList<EntityRelationshipBorrowerDto>( entityRelationshipType.size() );
        for ( EntityRelationshipType entityRelationshipType1 : entityRelationshipType ) {
            list.add( toEntityRelationshipBorrowerDto( entityRelationshipType1 ) );
        }

        return list;
    }

    private Long entityRelationshipTypeDtoEntityRelationConfigDetailConfigId(EntityRelationshipTypeDto entityRelationshipTypeDto) {
        if ( entityRelationshipTypeDto == null ) {
            return null;
        }
        LosConfigDetails entityRelationConfigDetail = entityRelationshipTypeDto.getEntityRelationConfigDetail();
        if ( entityRelationConfigDetail == null ) {
            return null;
        }
        Long configId = entityRelationConfigDetail.getConfigId();
        if ( configId == null ) {
            return null;
        }
        return configId;
    }

    private Long entityRelationshipDtoEntityRelationConfigDetailConfigId(EntityRelationshipDto entityRelationshipDto) {
        if ( entityRelationshipDto == null ) {
            return null;
        }
        LosConfigDetails entityRelationConfigDetail = entityRelationshipDto.getEntityRelationConfigDetail();
        if ( entityRelationConfigDetail == null ) {
            return null;
        }
        Long configId = entityRelationConfigDetail.getConfigId();
        if ( configId == null ) {
            return null;
        }
        return configId;
    }

    protected List<EntityNodeDto> parentEntityNodeListToEntityNodeDtoList(List<ParentEntityNode> list) {
        if ( list == null ) {
            return null;
        }

        List<EntityNodeDto> list1 = new ArrayList<EntityNodeDto>( list.size() );
        for ( ParentEntityNode parentEntityNode : list ) {
            list1.add( toEntityNodeDto( parentEntityNode ) );
        }

        return list1;
    }

    protected EntityRelationshipTypeDto entityRelationshipTypeToEntityRelationshipTypeDto(EntityRelationshipType entityRelationshipType) {
        if ( entityRelationshipType == null ) {
            return null;
        }

        EntityRelationshipTypeDto entityRelationshipTypeDto = new EntityRelationshipTypeDto();

        entityRelationshipTypeDto.setEntityRelationshiptypeId( entityRelationshipType.getEntityRelationshiptypeId() );
        entityRelationshipTypeDto.setEntityId1( entityRelationshipType.getEntityId1() );
        entityRelationshipTypeDto.setParentEntity( entityRelationshipType.getParentEntity() );
        entityRelationshipTypeDto.setEntityId2( entityRelationshipType.getEntityId2() );
        entityRelationshipTypeDto.setChildEntity( entityRelationshipType.getChildEntity() );
        entityRelationshipTypeDto.setRelationshiptypeId( entityRelationshipType.getRelationshiptypeId() );
        entityRelationshipTypeDto.setEntityRelationConfigDetail( entityRelationshipType.getEntityRelationConfigDetail() );
        entityRelationshipTypeDto.setOwnership( entityRelationshipType.getOwnership() );
        entityRelationshipTypeDto.setStatus( entityRelationshipType.isStatus() );

        return entityRelationshipTypeDto;
    }

    private String entityRelationshipTypeChildEntityEntityId(EntityRelationshipType entityRelationshipType) {
        if ( entityRelationshipType == null ) {
            return null;
        }
        FinancialEntity childEntity = entityRelationshipType.getChildEntity();
        if ( childEntity == null ) {
            return null;
        }
        String entityId = childEntity.getEntityId();
        if ( entityId == null ) {
            return null;
        }
        return entityId;
    }

    private String entityRelationshipTypeChildEntityEntityTypeConfigConfigCode(EntityRelationshipType entityRelationshipType) {
        if ( entityRelationshipType == null ) {
            return null;
        }
        FinancialEntity childEntity = entityRelationshipType.getChildEntity();
        if ( childEntity == null ) {
            return null;
        }
        LosConfigDetails entityTypeConfig = childEntity.getEntityTypeConfig();
        if ( entityTypeConfig == null ) {
            return null;
        }
        String configCode = entityTypeConfig.getConfigCode();
        if ( configCode == null ) {
            return null;
        }
        return configCode;
    }

    private String entityRelationshipTypeEntityRelationConfigDetailConfigCode(EntityRelationshipType entityRelationshipType) {
        if ( entityRelationshipType == null ) {
            return null;
        }
        LosConfigDetails entityRelationConfigDetail = entityRelationshipType.getEntityRelationConfigDetail();
        if ( entityRelationConfigDetail == null ) {
            return null;
        }
        String configCode = entityRelationConfigDetail.getConfigCode();
        if ( configCode == null ) {
            return null;
        }
        return configCode;
    }
}
