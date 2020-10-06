package com.idexcel.cync.los.entity.service;

import com.idexcel.cync.los.entity.model.ClientEntity;

public interface DBQueriesService {

	public ClientEntity fetchClientByName(String clientName);

}
