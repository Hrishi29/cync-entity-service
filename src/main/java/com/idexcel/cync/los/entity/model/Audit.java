package com.idexcel.cync.los.entity.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.view.View;

import lombok.Getter;
import lombok.Setter;

/**
 * Model class for defining auditing data
 * 
 * @author Idexcel
 *
 * @param <U>
 */

@Embeddable
@Getter
@Setter
public class Audit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "created_by", updatable = false, nullable = false)
	@JsonIgnore
	private String createdBy;

	@JsonView({ View.AllFields.class })
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "modified_by", nullable = false)
	@JsonIgnore
	private String modifiedBy;

	@Column(name = "modified_at")
	@JsonIgnore
	private LocalDateTime modifiedAt;
}
