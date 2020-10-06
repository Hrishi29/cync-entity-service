package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

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
public class CommercialEntityList extends FinancialEntityList implements LosAuditable, Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "business_name")
	private String businessName;

	@Embedded
	private LosAudit losAudit;

//	@ManyToOne
//	@JoinColumn(name = "business_type_id", referencedColumnName = "config_id")
//	@JsonManagedReference
//	private LosConfigDetails businessType;

}
