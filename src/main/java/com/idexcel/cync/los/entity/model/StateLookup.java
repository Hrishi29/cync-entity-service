package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vw_state_provinces")
@Getter
@Setter
public class StateLookup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "state_id")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class,View.EntityRelationListView.class })
	private Long stateId;

	@Column(name = "country_id")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class,View.EntityRelationListView.class })
	private Long countryId;

	@Column(name = "state_code")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class,View.EntityRelationListView.class })
	private String stateCode;

	@Column(name = "state_name")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class,View.EntityRelationListView.class })
	private String stateName;
	
	@ManyToOne
	@JoinColumn(name = "country_id", referencedColumnName = "country_id", insertable =false, updatable = false)
	private CountryLookup countryLookup;

}
