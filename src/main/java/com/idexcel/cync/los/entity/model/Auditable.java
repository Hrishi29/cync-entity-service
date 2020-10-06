package com.idexcel.cync.los.entity.model;

/**
 * Model class for defining auditing data
 * 
 * @author Amit
 *
 */

public interface Auditable {

	Audit getAudit();

	void setAudit(Audit audit);

}