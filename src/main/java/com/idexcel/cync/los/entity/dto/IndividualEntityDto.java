package com.idexcel.cync.los.entity.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.view.View;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndividualEntityDto extends FinancialEntityDto {

	@ApiModelProperty(notes = "date of birth", example = "06/30/1995", required = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOSEntityConstants.DATE_FORMAT)
	@Pattern(regexp = LOSEntityConstants.DATE_PATTERN, message = "error.Date.format")
	@JsonView({ View.AllFields.class, View.IndividualEntityView.class })
	@Size(min = 10, max = 10, message = "error.dateOfBirth.length")
	private String dateOfBirth;

	@NotNull(message = "error.firstName.value")
	@Pattern(regexp = LOSEntityConstants.INDIVIDUAL_ENTITY_NAME_REGEX, message = "error.firstName.format")//NOSONAR
	@JsonView({ View.AllFields.class, View.IndividualEntityView.class, View.EntityNameSearchView.class,
			View.EntityTreeDataView.class})
	private String firstName;

	@Size(max = 1, message = "error.name.middleNamelength")
	@Pattern(regexp = LOSEntityConstants.INDIVIDUAL_ENTITY_NAME_REGEX, message = "error.middleName.format")//NOSONAR
	@JsonView({ View.AllFields.class, View.IndividualEntityView.class, View.EntityNameSearchView.class,
			View.EntityTreeDataView.class})
	private String middleName;

	@Pattern(regexp = LOSEntityConstants.INDIVIDUAL_ENTITY_NAME_REGEX, message = "error.lastName.format")//NOSONAR
	@NotNull(message = "error.lastName.value")
	@JsonView({ View.AllFields.class, View.IndividualEntityView.class, View.EntityNameSearchView.class,
			View.EntityTreeDataView.class})
	private String lastName;

}
