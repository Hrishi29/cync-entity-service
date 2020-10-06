package com.idexcel.cync.los.entity.validator;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.CLIENT_DETAILS_NOT_FOUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.dao.ClientDetailsRepository;
import com.idexcel.cync.los.entity.exception.ResourceNotFoundException;
import com.idexcel.cync.los.entity.model.ClientEntity;
import com.idexcel.cync.los.entity.model.EntityRelationshipType;
import com.idexcel.cync.los.entity.model.FinancialEntity;

@Service
public class ClientValidator {

	private final ClientDetailsRepository clientDetailsRepository;
	private final MessageSource errorMsgs;

	@Autowired
	public ClientValidator(ClientDetailsRepository clientDetailsRepository, MessageSource errorMsgs) {
		this.clientDetailsRepository = clientDetailsRepository;
		this.errorMsgs = errorMsgs;
	}

	public void isValidClient(EntityRelationshipType entityRelationshipType) {
		if (isClientExistInDb() != null) {
			entityRelationshipType.setClientDetail(isClientExistInDb());
		}
	}

	public void isValidClient(FinancialEntity entity) {
		if (isClientExistInDb() != null) {
			entity.setClientDetail(isClientExistInDb());
		}
	}

	public ClientEntity isClientExistInDb() {
		Long clientId = CommonUtils.getClientIdFromMDC();
		if (clientId != null) {
			return clientDetailsRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException(errorMsgs
					.getMessage(CLIENT_DETAILS_NOT_FOUND, new Object[] { clientId }, CommonUtils.getLocale())));
		}
		return null;
	}
}
