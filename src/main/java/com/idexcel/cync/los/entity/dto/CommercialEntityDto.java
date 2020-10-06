package com.idexcel.cync.los.entity.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.view.View;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommercialEntityDto extends FinancialEntityDto {

	@NotEmpty(message = "error.businessName.value")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.EntityNameSearchView.class,
			View.EntityTreeDataView.class})
	private String businessName;

	@ApiModelProperty(notes = "Business open date", example = "06/30/1995")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOSEntityConstants.DATE_FORMAT)
	@Pattern(regexp = LOSEntityConstants.DATE_PATTERN, message = "error.Date.format")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private String businessOpenDate;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private NaicsDto naics;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.EntityTreeDataView.class,})
	private LosBusinessTypeDto businessType;

}
