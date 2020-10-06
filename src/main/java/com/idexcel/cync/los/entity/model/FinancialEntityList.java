package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Model class for Financial Entity
 * 
 * @author Idexcel
 * @see IndividualFinancialEntity
 * @see CommercialEntity
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_entity")
@Setter
@Getter
@EntityListeners(AuditListener.class)
public class FinancialEntityList implements Auditable, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "entity_id")
	private String entityId;

	@Column(name = "entity_type_id")
	private Long entityTypeId;

	@ManyToOne
	@JoinColumn(name = "entity_type_id", referencedColumnName = "config_id", insertable = false, updatable = false)
	private LosConfigDetails entityTypeConfig;

//	@ManyToOne
//	@JoinColumn(name = "client_id", referencedColumnName = "client_id")
//	@JsonBackReference
//	private ClientEntity clientDetail;

	@Column(name = "status")
	private boolean status = true;

	@Column(name = "rm_email")
	private String relationshipManagerEmail;

	@Embedded
	private Audit audit;

}
