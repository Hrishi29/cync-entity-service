package com.idexcel.cync.los.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityRelationshipBorrowerDto {

	private String entityId;
	private String entityName;
	private String entityType;
	private String relation;
	private Double ownership;

}
