package com.idexcel.cync.los.entity.validator;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_COUNTRY_ID;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_STATE_FOR_COUNTRYID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.utils.ActivityLog;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.common.utils.Operation;
import com.idexcel.cync.los.entity.common.utils.Status;
import com.idexcel.cync.los.entity.dao.CountryLookupRepository;
import com.idexcel.cync.los.entity.dao.StateLookupRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.model.CountryLookup;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.StateLookup;

@Service
public class FSCountryStateValidator {

	private static final Logger LOG = LoggerFactory.getLogger(FSCountryStateValidator.class);

	private CountryLookupRepository countryLookupRepository;
	private StateLookupRepository stateLookupRepository;
	private final MessageSource errorMsgs;

	@Autowired
	public FSCountryStateValidator(CountryLookupRepository countryLookupRepository,
			StateLookupRepository stateLookupRepository, MessageSource errorMsgs) {
		this.countryLookupRepository = countryLookupRepository;
		this.stateLookupRepository = stateLookupRepository;
		this.errorMsgs = errorMsgs;
	}

	/**
	 * Method to Validate Country & State
	 * 
	 * @param entity
	 */
	public void isCountryStateNullOrNot(FinancialEntity entity) {
		CountryLookup countryObj = entity.getCountry();
		StateLookup stateObj = entity.getState();
		// Condition to Validate CID Null SID null
		if ((countryObj == null || entity.getCountry().getCountryId() == null)
				&& (stateObj == null || entity.getState().getStateId() == null)) {
			entity.setCountry(null);
			entity.setState(null);
		}
		// Condition to Validate CID Null SID NotNull
		else if ((entity.getCountry().getCountryId() == null) && (entity.getState().getStateId() != null)) {
			throw new BadRequestException("Please Selcet Country to Select State");
		}
		// Condition to Validate (CID NotNull) & setCID Null
		else if ((entity.getCountry().getCountryId() != null)
				&& (stateObj == null || entity.getState().getStateId() == null)) {
			isValidCountryId(entity.getCountry().getCountryId());
			entity.setState(null);
		}
		// Condition to Validate (CID NotNull) and SID(NotNull)
		else if ((entity.getCountry().getCountryId() != null) && (entity.getState().getStateId() != null)) {
			isValidCountryId(entity.getCountry().getCountryId());
			isValidStateId(entity);
		}
	}

	public void isValidStateId(FinancialEntity entity) {
		StateLookup state = stateLookupRepository.findByStateIdAndCountryId(entity.getState().getStateId(),
				entity.getCountry().getCountryId());
		if (state == null) {
			LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
					"Entered Invalid State to the Country", Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
			throw new BadRequestException(
					errorMsgs
							.getMessage(INVALID_STATE_FOR_COUNTRYID,
									new Object[] { String.valueOf(entity.getState().getStateId()),
											String.valueOf(entity.getCountry().getCountryId()) },
									CommonUtils.getLocale()));
		}
	}

	/**
	 * Method to Validate countryId
	 * 
	 * @param countryId
	 */
	public void isValidCountryId(Long countryId) {
		CountryLookup country = countryLookupRepository.findByCountryId(countryId);
		if (country == null) {
			LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
					"Entered Invalid CountryId", Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
			throw new BadRequestException(errorMsgs.getMessage(INVALID_COUNTRY_ID,
					new Object[] { String.valueOf(countryId) }, CommonUtils.getLocale()));
		}
	}

}
