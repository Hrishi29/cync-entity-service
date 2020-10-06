package com.idexcel.cync.los.entity.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

/**
 * Model class for Business Financial Entity
 * 
 * @author Amit
 * @see FinancialEntity
 * @see IndividualFinancialEntity
 */
@Entity
@Table(name = "tb_commercial_entity_details")
@Setter
@Getter
@EntityListeners(LosAuditListener.class)
public class CommercialEntity extends FinancialEntity implements LosAuditable, Serializable {

	private static final long serialVersionUID = 1L;

	@JsonView({ View.EntityTreeDataView.class, View.EntityRelationListView.class })
	@Column(name = "business_name")
	private String businessName;

	@Column(name = "business_open_date")
	private Date businessOpenDate;

	@ManyToOne
	@JoinColumn(name = "naics_code_id", referencedColumnName = "config_id")
	@JsonManagedReference
	private LosConfigDetails naics;

	@Embedded
	private LosAudit losAudit;

	@ManyToOne
	@JoinColumn(name = "business_type_id", referencedColumnName = "config_id")
	@JsonManagedReference
	@JsonView({ View.EntityRelationListView.class, View.EntityTreeDataView.class })
	private LosConfigDetails businessType;

}
