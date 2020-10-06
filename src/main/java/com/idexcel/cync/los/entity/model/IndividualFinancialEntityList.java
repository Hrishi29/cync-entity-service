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
 * Model class for Individual Financial Entity
 * 
 * @author 
 * @see CommercialEntity
 * @see FinancialEntity
 */

@Entity
@Table(name = "tb_individual_entity_details")
@Setter
@Getter
@EntityListeners(LosAuditListener.class)
public class IndividualFinancialEntityList extends FinancialEntityList implements LosAuditable, Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Embedded
	private LosAudit losAudit;

}
