package com.idexcel.cync.los.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_client")
@Getter
@Setter
public class ClientEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "client_name")
	private String clientName;
	
	@Column(name = "client_code")
	private String clientCode;

	@Column(name = "status")
	private boolean status;

}
