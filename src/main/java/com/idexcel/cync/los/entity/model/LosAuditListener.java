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

public class LosAuditListener {

	@PrePersist
	public void setCreatedAt(LosAuditable auditable) {
		LosAudit losAudit = auditable.getLosAudit();
		if (losAudit == null) {
			losAudit = new LosAudit();
			auditable.setLosAudit(losAudit);
		}
		losAudit.setLosCreatedAt(LocalDateTime.now());
		losAudit.setLosCreatedBy(AuditorAwareImpl.getUserName());
	}

	@PreUpdate
	public void setUpdatedOn(LosAuditable auditable) {
		LosAudit losAudit = auditable.getLosAudit();
		if (losAudit == null) {
			losAudit = new LosAudit();
			auditable.setLosAudit(losAudit);
		}
		losAudit.setLosModifiedAt(LocalDateTime.now());
		losAudit.setLosModifiedBy(AuditorAwareImpl.getUserName());
	}
}
