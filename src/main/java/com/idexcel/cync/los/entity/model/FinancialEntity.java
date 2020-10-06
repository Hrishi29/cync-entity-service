package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.config.EntitySequenceIdGenerator;
import com.idexcel.cync.los.entity.view.View;

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
public class FinancialEntity implements Auditable, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "entity_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_entity_entity_id_seq")
	@GenericGenerator(name = "tb_entity_entity_id_seq", strategy = "com.idexcel.cync.los.entity.config.EntitySequenceIdGenerator", parameters = {
			@Parameter(name = EntitySequenceIdGenerator.INCREMENT_PARAM, value = LOSEntityConstants.UNIQUE_KEY_INCREMENT_VALUE),
			@Parameter(name = EntitySequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = LOSEntityConstants.UNIQUE_KEY_LENGTH) })
	@JsonView({ View.EntityTreeDataView.class, View.EntityRelationListView.class})
	private String entityId;

	@Column(name = "entity_type_id")
	private Long entityTypeId;

	@ManyToOne
	@JoinColumn(name = "entity_type_id", referencedColumnName = "config_id", insertable = false, updatable = false)
	@JsonView({ View.EntityRelationListView.class})
	private LosConfigDetails entityTypeConfig;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "notes")
	private String notes;

	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName = "client_id")
	@JsonBackReference
	private ClientEntity clientDetail;

	@Column(name = "status")
	private boolean status = true;

	@Column(name = "map")
	private String map;

	@Column(name = "city")
	private String city;

	@Column(name = "zip_code")
	private String zipCode;

	@ManyToOne
	@JoinColumn(name = "country_id", referencedColumnName = "country_id")
//	@JsonManagedReference  --commented by Amit
	private CountryLookup country;

	@ManyToOne
	@JoinColumn(name = "state_id", referencedColumnName = "state_id")
	// @JsonManagedReference --Commented by Amit
	private StateLookup state;

	@Column(name = "rm_email")
	private String relationshipManagerEmail;


	@Column(name = "address")
	private String address;

	@Embedded
	private Audit audit;

}
