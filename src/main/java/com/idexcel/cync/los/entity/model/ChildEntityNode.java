package com.idexcel.cync.los.entity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_entity_relationship_type_map")
public class ChildEntityNode implements Serializable {

	private static final long serialVersionUID = 1L;

	public ChildEntityNode() {
		super();
		this.children = new ArrayList<>();
	}

	public ChildEntityNode(String value, String childId, String parentId, FinancialEntity childEntity,
			FinancialEntity parentEntity) {
		this.childId = childId;
		this.parentId = parentId;
		this.children = new ArrayList<>();
		this.childEntity = childEntity;
		this.parentEntity = parentEntity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial", name = "entity_relationshiptype_id")
	private Long entityRelationshiptypeId;

	@Column(name = "entity_id1")
	private String childId;

	@OneToOne
	@JoinColumn(name = "entity_id1", referencedColumnName = "entity_id", insertable = false, updatable = false)
	private FinancialEntity childEntity;

	@Column(name = "entity_id2")
	private String parentId;

	@ManyToOne
	@JoinColumn(name = "entity_id2", referencedColumnName = "entity_id", insertable = false, updatable = false)
	private FinancialEntity parentEntity;

	@JoinColumn(name = "entity_id2", referencedColumnName = "entity_id1", insertable = false, updatable = false)
	@ManyToOne
	private ChildEntityNode parent;

	@JoinColumn(name = "entity_id2", referencedColumnName = "entity_id1", insertable = false, updatable = false)
	@OneToMany(fetch = FetchType.EAGER)
	private List<ChildEntityNode> children;

	@ManyToOne
	@JoinColumn(name = "relationshiptype_id", referencedColumnName = "config_id", insertable = false, updatable = false)
	private LosConfigDetails parentConfigDetails;
	
	@Column(name = "status")
	private boolean active = true;
	
	@Column(name = "deleted")
	private boolean deleted;

	public LosConfigDetails getParentConfigDetails() {
		return parentConfigDetails;
	}

	public void setParentConfigDetails(LosConfigDetails parentConfigDetails) {
		this.parentConfigDetails = parentConfigDetails;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public ChildEntityNode getParent() {
		return parent;
	}

	public void setParent(ChildEntityNode parent) {
		this.parent = parent;
	}

	public List<ChildEntityNode> getChildren() {
		
		List<ChildEntityNode> childList= children.stream().distinct().collect(Collectors.toList());
		return childList.stream().filter(x -> x.active == Boolean.TRUE).collect(Collectors.toList());
	}

	public void setChildren(List<ChildEntityNode> children) {
		this.children = children;
	}

	public void addChild(ChildEntityNode child) {
		if (!this.children.contains(child) && child != null)
			this.children.add(child);
	}

	public void setParentEntity(FinancialEntity parentEntity) {
		this.parentEntity = parentEntity;
	}

	public void setChildEntity(FinancialEntity childEntity) {
		this.childEntity = childEntity;

	}

	public FinancialEntity getParentEntity() {
		return parentEntity;
	}

	public FinancialEntity getChildEntity() {
		return childEntity;

	}

}
