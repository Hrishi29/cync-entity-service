package com.idexcel.cync.los.entity.dto;

import java.time.LocalDateTime;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.model.Audit;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.StateLookup;
import com.idexcel.cync.los.entity.view.View;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Inheritance(strategy = InheritanceType.JOINED)

@Setter
@Getter
public class FinancialEntityDto {
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class,
			View.EntityNameSearchView.class, View.EntityTreeDataView.class })
	private String entityId;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class })
	private LosConfigDetails entityTypeConfig;

	@Pattern(regexp = LOSEntityConstants.ENTITY_PHONE_REGEX, message = "error.businessPhoneNumber.format")
	@Size(min = 10, max = 10, message = "error.businessPhoneNumber.length")
	@ApiModelProperty(notes = "Phone .No Releted to Entity ", required = true)
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class })
	private String phone;

	@Size(min = 7, max = 50, message = "error.email.length")
	@Email(message = "error.emailFormat.value")
	@Pattern(regexp = LOSEntityConstants.EMAIL_REGEX, message = "error.emailFormat.value") // NOSONAR
	@ApiModelProperty(notes = "Email for entity", required = true)
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class })
	private String email;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class })
	@ApiModelProperty(notes = "Phone .No Releted to Entity ")
	private String map;

	@Size(max = 255, message = "error.notes.value")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class })
	private String notes;

	@JsonIgnore
	private LocalDateTime createdAt;

	@JsonIgnore
	private String createdBy;

	@JsonIgnore
	private LocalDateTime modifiedAt;

	@JsonIgnore
	private String modifiedBy;

	@ApiModelProperty(notes = "Entity status is for entity is Activated or Deactivated")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class })
	private boolean status = true;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class })
	private String relationshipManagerEmail;

	@Size(max = 100, message = "error.entityAddress.length")
	@Pattern(regexp = LOSEntityConstants.ENTITY_ADDRESS_REGEX, message = "error.entityAddress.format")
	@ApiModelProperty(notes = "Address of Entity", required = true)
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private String address;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private CountryLookup country;

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private StateLookup state;

	@Size(max = 50, message = "error.city.length")
	@Pattern(regexp = LOSEntityConstants.ENTITY_CITY_REGEX, message = "error.entityCity.format")
	@ApiModelProperty(notes = "Entity belongs to which city", required = true)
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private String city;

	@Pattern(regexp = LOSEntityConstants.ZIPCODE_FORMAT, message = "error.zipcode.length") // NOSONAR
	@ApiModelProperty(notes = "Entity belongs to which zipcodeId")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class })
	private String zipCode;

	@JsonView({ View.AllFields.class })
	private Audit audit;
}
