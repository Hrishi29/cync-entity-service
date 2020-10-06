package com.idexcel.cync.los.entity.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LosConfigDetailsDto {

	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class, View.EntityNameSearchView.class, View.EntityTreeDataView.class })
	private Long configId;
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class, View.EntityNameSearchView.class, View.EntityTreeDataView.class })
	private String configCode;
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class, View.EntityNameSearchView.class, View.EntityTreeDataView.class })
	private Long configtypeId;
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class, View.EntityNameSearchView.class, View.EntityTreeDataView.class })
	private String configtypeCode;
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class, View.EntityNameSearchView.class, View.EntityTreeDataView.class })
	private String configValue;

}
