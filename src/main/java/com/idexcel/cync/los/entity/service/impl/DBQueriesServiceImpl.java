package com.idexcel.cync.los.entity.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.dao.ClientDetailsRepository;
import com.idexcel.cync.los.entity.model.ClientEntity;
import com.idexcel.cync.los.entity.service.DBQueriesService;

@Service
public class DBQueriesServiceImpl implements DBQueriesService {

	private static final Logger LOG = LoggerFactory.getLogger(DBQueriesServiceImpl.class);
	private final ClientDetailsRepository clientDetailsRepository;

	@Autowired
	public DBQueriesServiceImpl(ClientDetailsRepository clientDetailsRepository) {
		this.clientDetailsRepository = clientDetailsRepository;
	}

	@Override
	public ClientEntity fetchClientByName(String clientName) {
		ClientEntity clientInfo = clientDetailsRepository.findByClientNameAndStatus(clientName, true);
		if (clientInfo != null) {
			LOG.debug("Fetched ClientDetails by client name :{} ",clientName);
			return clientInfo;
		}
		return null;
	}
}
