package com.idexcel.cync.los.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommercialEntityListDto extends FinancialEntityListDto {

	@JsonView({ View.AllFields.class})
	private String businessName;

	@JsonView({ View.AllFields.class})
	private LosBusinessTypeDto businessType;

}
