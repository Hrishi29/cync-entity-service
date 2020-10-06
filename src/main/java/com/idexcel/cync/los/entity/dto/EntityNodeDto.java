package com.idexcel.cync.los.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityNodeDto {

	@JsonView({View.EntityTreeDataView.class })
    private String parentId;
	
	@JsonView({View.EntityTreeDataView.class })
    private FinancialEntity parentEntity;
	
	@JsonView({View.EntityTreeDataView.class })
	private String childId;
	
	@JsonView({View.EntityTreeDataView.class })
	private FinancialEntity childEntity;
	
	@JsonView({ View.EntityTreeDataView.class })
	private LosConfigDetailsDto losConfigDetails;
	
	@JsonView({View.EntityTreeDataView.class })
    private List<EntityNodeDto> children;

}
