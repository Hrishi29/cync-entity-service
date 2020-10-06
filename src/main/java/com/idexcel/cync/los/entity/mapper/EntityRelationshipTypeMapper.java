package com.idexcel.cync.los.entity.mapper;

import java.util.List;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.idexcel.cync.los.entity.dto.EntityNodeDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipBorrowerDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipTypeDto;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;
import com.idexcel.cync.los.entity.model.ParentEntityNode;

/**
 * 
 * @author Idexcel
 *
 */

@Mapper(uses = { LookupDataMapper.class, FinancialEntityMapper.class, LosConfigMapper.class })
public interface EntityRelationshipTypeMapper {// NOSONAR
	
	EntityRelationshipTypeMapper INSTANCE = Mappers.getMapper(EntityRelationshipTypeMapper.class);

	@Mapping(target = "relationshiptypeId", source = "entityRelationConfigDetail.configId")
	EntityRelationshipType toEntityRelationshipType(EntityRelationshipTypeDto entityRelationshipTypeDto);

	@Mapping(target = "relationshiptypeId", source = "entityRelationConfigDetail.configId")
	EntityRelationshipType toEntityRelationshipCreteType(EntityRelationshipDto entityRelationshipDto);

	@Mapping(target = "parentId", source = "parentId")
	@Mapping(target = "parentEntity", source = "parentEntity")
	@Mapping(target = "childId", source = "childId")
	@Mapping(target = "childEntity", source = "childEntity")
	@Mapping(target = "losConfigDetails", source = "losConfigDetails")
	@Mapping(target = "children", source = "children")
	EntityNodeDto toEntityNodeDto(ParentEntityNode parentEntityNode);

	List<EntityRelationshipTypeDto> toEntityRelationshipTypeDto(List<EntityRelationshipType> entityRelationshipType);

	@BeforeMapping
	default void getEntityType(@MappingTarget EntityRelationshipBorrowerDto target, EntityRelationshipType source) {
		if (source.getChildEntity() instanceof CommercialEntity) {
			target.setEntityName(((CommercialEntity) source.getChildEntity()).getBusinessName());
		} else if (source.getChildEntity() instanceof IndividualFinancialEntity) {
			String fName = ((IndividualFinancialEntity) source.getChildEntity()).getFirstName();
			String mName = ((IndividualFinancialEntity) source.getChildEntity()).getMiddleName();
			String lName = ((IndividualFinancialEntity) source.getChildEntity()).getLastName();
			if (mName == null) {
				mName = " ";
				target.setEntityName(fName + mName + lName);
			} else {
				target.setEntityName(fName + " " + mName + " " + lName);
			}
		}
	}
	@Mapping(target = "entityId", source = "childEntity.entityId")
	@Mapping(target = "entityName", ignore = true)
	@Mapping(target = "entityType", source = "childEntity.entityTypeConfig.configCode")
	@Mapping(target = "relation", source = "entityRelationConfigDetail.configCode")
	EntityRelationshipBorrowerDto toEntityRelationshipBorrowerDto(EntityRelationshipType entityRelationshipType);

	List<EntityRelationshipBorrowerDto> toEntityRelationshipBorrowerDto(
			List<EntityRelationshipType> entityRelationshipType);

}
