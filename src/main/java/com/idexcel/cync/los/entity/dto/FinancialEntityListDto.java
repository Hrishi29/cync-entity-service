package com.idexcel.cync.los.entity.dto;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.model.Audit;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.view.View;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Inheritance(strategy = InheritanceType.JOINED)

@Setter
@Getter
public class FinancialEntityListDto {
	
	@JsonView({ View.AllFields.class })
	private String entityId;

	@JsonView({ View.AllFields.class })
	private LosConfigDetails entityTypeConfig;

	@ApiModelProperty(notes = "Entity status is for entity is Activated or Deactivated")
	@JsonView({ View.AllFields.class})
	private boolean status = true;

	@JsonView({ View.AllFields.class })
	private String relationshipManagerEmail;

	@JsonView({ View.AllFields.class })
	private Audit audit;
}
