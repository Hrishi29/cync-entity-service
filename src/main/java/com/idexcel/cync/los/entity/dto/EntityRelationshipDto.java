package com.idexcel.cync.los.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idexcel.cync.los.entity.model.LosConfigDetails;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityRelationshipDto {

	private String entityId2;
	@JsonIgnore
	private Long relationshiptypeId;
	private Double ownership;
	private LosConfigDetails entityRelationConfigDetail;
	private boolean status = true;

}
