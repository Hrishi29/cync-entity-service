package com.idexcel.cync.los.entity.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.web.ProjectedPayload;

@ProjectedPayload
public interface EntityNodeSpouse {
	
	@Value("#{target.entity_id}")
	String getEntity_id();

	@Value("#{target.first_name}")
	String getFirst_name();

	@Value("#{target.last_name}")
	String getLast_name();

}
