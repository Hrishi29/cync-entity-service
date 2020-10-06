package com.idexcel.cync.los.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LosBusinessTypeDto {

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private Long businessTypeId;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private String businessCode;

	@JsonView({ View.CommercialEntityView.class })
	private String configTypeCode;

	@JsonView({ View.CommercialEntityView.class })
	private String configValue;

}
