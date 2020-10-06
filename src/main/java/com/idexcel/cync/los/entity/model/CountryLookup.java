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
@Table(name = "vw_countries")
@Getter
@Setter
public class CountryLookup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "country_id")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class,View.EntityRelationListView.class })
	private Long countryId;

	@Column(name = "country_code")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class,View.EntityRelationListView.class })
	private String countryCode;

	@Column(name = "country_name")
	@JsonView({ View.AllFields.class, View.CommercialEntityView.class,View.EntityRelationListView.class })
	private String countryName;

}
