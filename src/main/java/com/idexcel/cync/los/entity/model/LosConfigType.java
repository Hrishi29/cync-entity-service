package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_configtype")
@Getter
@Setter
public class LosConfigType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "configtype_id")
	private Long configTypeId;

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "configtype_code")
	private String configtypeCode;

	@Column(name = "configtype_name")
	private String configtypeName;

}
