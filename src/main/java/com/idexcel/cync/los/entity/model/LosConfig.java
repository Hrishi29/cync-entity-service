package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_config")
@Getter
@Setter
public class LosConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "config_id")
	private Long configId;

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "configtype_id")
	private Long configtypeId;

	@Column(name = "config_code")
	private String configCode;

	@Column(name = "config_value")
	private String configValue;

}
