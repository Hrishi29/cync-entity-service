package com.idexcel.cync.los.entity.model;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.idexcel.cync.los.entity.config.AuditorAwareImpl;

/**
 * Model class for defining auditing data
 * 
 * @author Idexcel
 *
 */

public class AuditListener {

	@PrePersist
	public void setCreatedAt(Auditable auditable) {
		Audit audit = auditable.getAudit();
		if (audit == null) {
			audit = new Audit();
			auditable.setAudit(audit);
		}
		audit.setCreatedAt(LocalDateTime.now());
		audit.setCreatedBy(AuditorAwareImpl.getUserName());
	}

	@PreUpdate
	public void setUpdatedOn(Auditable auditable) {
		Audit audit = auditable.getAudit();
		if (audit == null) {
			audit = new Audit();
			auditable.setAudit(audit);
		}
		audit.setModifiedAt(LocalDateTime.now());
		audit.setModifiedBy(AuditorAwareImpl.getUserName());
	}
}
