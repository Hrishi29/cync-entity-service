package com.idexcel.cync.los.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndividualEntityListDto extends FinancialEntityListDto {

	@JsonView({ View.AllFields.class})
	private String firstName;

	@JsonView({ View.AllFields.class})
	private String middleName;

	@JsonView({ View.AllFields.class})
	private String lastName;

}
