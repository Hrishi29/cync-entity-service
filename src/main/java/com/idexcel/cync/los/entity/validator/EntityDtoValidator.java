package com.idexcel.cync.los.entity.validator;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.COMMERCIAL_ENTITY_NAME_FORMAT;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.COMMERCIAL_ENTITY_NAME_LENGTH;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.INVALID_DATE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.ActivityLog;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.common.utils.Operation;
import com.idexcel.cync.los.entity.common.utils.Status;
import com.idexcel.cync.los.entity.dto.CommercialEntityDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityDto;
import com.idexcel.cync.los.entity.exception.BadRequestException;

@Service
public class EntityDtoValidator {

	private static final Logger LOG = LoggerFactory.getLogger(EntityDtoValidator.class);

	public final MessageSource errorMsgs;

	@Autowired
	public EntityDtoValidator(MessageSource errorMsgs) {
		this.errorMsgs = errorMsgs;
	}

	/**
	 * Method to Validate BusinessOpenDate of CommercialEntityDto
	 * 
	 * @param commercialEntityDto
	 * @return
	 */
	public CommercialEntityDto isvalidDate(CommercialEntityDto commercialEntityDto) {
		if (commercialEntityDto.getBusinessOpenDate() == null)
			commercialEntityDto.setBusinessOpenDate(null);
		else
			validateDate(commercialEntityDto.getBusinessOpenDate());
		return commercialEntityDto;
	}

	/**
	 * Method to Validate DateOfBirth of IndividualEntityDto
	 * 
	 * @param individualFinancialEntityDto
	 * @return
	 */
	public IndividualEntityDto isvalidDate(IndividualEntityDto individualFinancialEntityDto) {
		if (individualFinancialEntityDto.getDateOfBirth() == null)
			individualFinancialEntityDto.setDateOfBirth(null);
		else
			validateDate(individualFinancialEntityDto.getDateOfBirth());
		return individualFinancialEntityDto;
	}

	/**
	 * Method to Validate Date, Given date Cannot Be Greater than Last Date of the
	 * Month
	 * 
	 * @param dateToValidate
	 */
	private void validateDate(String dateToValidate) {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(LOSEntityConstants.DATE_FORMAT);
		if (!dateToValidate.equals(dateFormat.format(LocalDate.parse(dateToValidate, dateFormat)))) {
			LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT_OR_UPDATE, null,
					"Entered invalid date :" + dateToValidate, Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
			throw new BadRequestException(
					errorMsgs.getMessage(INVALID_DATE, new Object[] { dateToValidate }, CommonUtils.getLocale()));
		}
	}

	/**
	 * 
	 * @param commercialEntityDto
	 * @return
	 */
	public CommercialEntityDto isvalidBusinessName(CommercialEntityDto commercialEntityDto) {
		if (!commercialEntityDto.getBusinessName().isEmpty() && (commercialEntityDto.getBusinessName().length() < 3)) {
			LOG.info(ActivityLog.getActivityLog(commercialEntityDto.getBusinessName(), Operation.INSERT, null,
					"Invalid businessName length" + commercialEntityDto.getBusinessName(), Status.ERROR, null,
					ActivityLog.localDateTimeInUTC()));
			throw new BadRequestException(
					errorMsgs.getMessage(COMMERCIAL_ENTITY_NAME_LENGTH, null, CommonUtils.getLocale()));
		} else if (!commercialEntityDto.getBusinessName().isEmpty() && (commercialEntityDto.getBusinessName()
				.matches("^[a-zA-Z0-9!,\\\\#'&@\\-\\[\\]\\(\\)+%$\\?\\>\\<\\/ ]*[a-zA-Z0-9]+[a-zA-Z0-9!,\\\\#'&@\\-\\[\\]\\(\\)+%$\\?\\>\\<\\/ ]*$") == Boolean.FALSE)) {// NOSONAR
			LOG.info(ActivityLog.getActivityLog(commercialEntityDto.getBusinessName(), Operation.INSERT_OR_UPDATE, null,
					"Invalid businessName format", Status.ERROR, null, ActivityLog.localDateTimeInUTC()));
			throw new BadRequestException(
					errorMsgs.getMessage(COMMERCIAL_ENTITY_NAME_FORMAT, null, CommonUtils.getLocale()));
		}
		return commercialEntityDto;
	}

}
