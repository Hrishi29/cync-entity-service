package com.idexcel.cync.los.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LosConfigDto {

	private Long configId;
	private String clientId;
	private int configtypeId;
	private String configCode;
	private String configValue;

}
