package com.idexcel.cync.los.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryStateLookupDto {

	private Long countryId;
	private String countryName;
	private String countryCode;
	private Long stateId;
	private String stateCode;
	private String stateName;

}
