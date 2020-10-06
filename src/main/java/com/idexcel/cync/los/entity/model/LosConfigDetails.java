package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vw_config")
@Getter
@Setter

public class LosConfigDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "config_id")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class,View.EntityRelationListView.class})
	private Long configId;

	@Column(name = "config_code")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class,View.EntityRelationListView.class, View.EntityTreeDataView.class})
	private String configCode;
	
	@Column(name = "configtype_id")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class,View.EntityRelationListView.class })
	private Long configtypeId;

	@Column(name = "configtype_code")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class,View.EntityRelationListView.class })
	private String configtypeCode;

	@Column(name = "config_value")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class, View.IndividualEntityView.class,View.EntityRelationListView.class })
	private String configValue;

}
