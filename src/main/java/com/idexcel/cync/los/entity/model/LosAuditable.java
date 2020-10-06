package com.idexcel.cync.los.entity.model;

/**
 * Model class for defining auditing data
 * 
 * @author IDEXCL
 *
 */

public abstract interface LosAuditable {

	LosAudit getLosAudit();
	void setLosAudit(LosAudit losAudit);
	
}