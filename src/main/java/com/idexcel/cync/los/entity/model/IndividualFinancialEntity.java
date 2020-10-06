package com.idexcel.cync.los.entity.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

/**
 * Model class for Individual Financial Entity
 * 
 * @author Amit
 * @see CommercialEntity
 * @see FinancialEntity
 */

@Entity
@Table(name = "tb_individual_entity_details")
@Setter
@Getter
@EntityListeners(LosAuditListener.class)
public class IndividualFinancialEntity extends FinancialEntity implements LosAuditable, Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@JsonView({	View.EntityTreeDataView.class,View.EntityRelationListView.class })
	@Column(name = "first_name")
	private String firstName;
	
	@JsonView({	View.EntityTreeDataView.class,View.EntityRelationListView.class})
	@Column(name = "middle_name")
	private String middleName;

	@JsonView({	View.EntityTreeDataView.class,View.EntityRelationListView.class})
	@Column(name = "last_name")
	private String lastName;

	@Embedded
	private LosAudit losAudit;

}
