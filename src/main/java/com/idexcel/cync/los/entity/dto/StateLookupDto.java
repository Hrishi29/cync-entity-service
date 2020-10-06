package com.idexcel.cync.los.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateLookupDto {

	private Long countryId;
	private Long stateId;
	private String stateCode;
	private String stateName;

}
