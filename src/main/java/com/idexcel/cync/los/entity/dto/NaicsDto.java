package com.idexcel.cync.los.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaicsDto {

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private Long naicsCodeId;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private String naicsCode;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private String naicsCodeValue;
	
}
