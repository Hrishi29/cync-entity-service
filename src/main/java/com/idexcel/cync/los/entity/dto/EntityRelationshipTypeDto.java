package com.idexcel.cync.los.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityRelationshipTypeDto {

	@JsonView({ View.AllFields.class, View.EntityRelationListView.class })
	private Long entityRelationshiptypeId;

	@JsonView({ View.AllFields.class })
	private String entityId1;

	@JsonView({ View.AllFields.class, View.EntityRelationListView.class })
	private FinancialEntity parentEntity;

	@JsonView({ View.AllFields.class, View.EntityRelationListView.class })
	private String entityId2;

	@JsonView({ View.AllFields.class, View.EntityRelationListView.class })
	private FinancialEntity childEntity;

	// @JsonView({ View.AllFields.class})
	private Long relationshiptypeId;

	@JsonView({ View.AllFields.class, View.EntityRelationListView.class })
	private LosConfigDetails entityRelationConfigDetail;

	@JsonView({ View.AllFields.class, View.EntityRelationListView.class })
	private Double ownership;

	@JsonView({ View.AllFields.class, View.EntityRelationListView.class })
	private boolean status = true;

}
