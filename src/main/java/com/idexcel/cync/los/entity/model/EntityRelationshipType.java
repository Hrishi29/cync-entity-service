package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_entity_relationship_type_map")
@Setter
@Getter
@EntityListeners(AuditListener.class)
public class EntityRelationshipType implements Auditable, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial", name = "entity_relationshiptype_id")
	private Long entityRelationshiptypeId;

	@ManyToOne
	@JoinColumn(name = "client_id", referencedColumnName = "client_id")
	@JsonBackReference
	private ClientEntity clientDetail;

	@Column(name = "entity_id1")
	private String entityId1;
	
	@ManyToOne
	@JoinColumn(name = "entity_id1", referencedColumnName = "entity_id", insertable = false, updatable = false)
	private FinancialEntity parentEntity;

	@Column(name = "entity_id2")
	private String entityId2;
	
	@OneToOne
	@JoinColumn(name = "entity_id2", referencedColumnName = "entity_id", insertable = false, updatable = false)
	private FinancialEntity childEntity;

	@Column(name = "relationshiptype_id")
	private Long relationshiptypeId;
	
	
	@ManyToOne
	@JoinColumn(name = "relationshiptype_id", referencedColumnName = "config_id", insertable = false, updatable = false)
	private LosConfigDetails entityRelationConfigDetail;

	@Column(name = "ownership")
	private Double ownership;

	@Column(name = "status")
	private boolean status = true;
	
	@Column(name = "deleted")
	private boolean deleted;

	@Embedded
	private Audit audit;

}
