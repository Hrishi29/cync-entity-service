package com.idexcel.cync.los.entity.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.ActivityLog;
import com.idexcel.cync.los.entity.common.utils.Operation;
import com.idexcel.cync.los.entity.common.utils.Status;
import com.idexcel.cync.los.entity.dto.CountryLookupDto;
import com.idexcel.cync.los.entity.dto.CountryStateLookupDto;
import com.idexcel.cync.los.entity.dto.LosConfigDetailsDto;
import com.idexcel.cync.los.entity.dto.LosConfigTypeDto;
import com.idexcel.cync.los.entity.dto.StateLookupDto;
import com.idexcel.cync.los.entity.mapper.LookupDataMapper;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.model.LosConfigType;
import com.idexcel.cync.los.entity.service.ConfigLookupService;
import com.idexcel.cync.los.entity.service.CountryLookupService;
import com.idexcel.cync.los.entity.service.StateLookupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin // NOSONAR
@RequestMapping(value = CoreConstants.API_BASE_PATH + "/lookup/")
@Api(tags = {"LookupData Controller"}, description = "LookupDataController : used to Access LookupData API of LOS Entity")//NOSONAR
class LookupDataController {

	private static final Logger LOG = LoggerFactory.getLogger(LookupDataController.class);
	private final CountryLookupService countryLookupService;
	private final StateLookupService stateLookupService;
	private final LookupDataMapper lookupDataMapper;
	private final ConfigLookupService configLookupService;

	@Autowired
	public LookupDataController(CountryLookupService countryLookupService, StateLookupService stateLookupService,
			LookupDataMapper lookupDataMapper, ConfigLookupService configLookupService) {
		this.countryLookupService = countryLookupService;
		this.stateLookupService = stateLookupService;
		this.lookupDataMapper = lookupDataMapper;
		this.configLookupService = configLookupService;
	}

	/**
	 * API to get Get countryList
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/countries")
	@ApiOperation(value = "${ApiOperation.LookupDataController.getAllCountry}", response = CountryLookupDto.class)
	public ResponseEntity<List<CountryLookupDto>> getAllCountry(HttpServletRequest request,
			HttpServletResponse response) {
		List<CountryLookupDto> countryLookupDtoList = countryLookupService.findCountryList().stream()
				.map(lookupDataMapper::toCountryLookupDto).collect(Collectors.toList());
		LOG.debug("countryList");
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, null, "Get CountryList",
				Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(countryLookupDtoList, HttpStatus.OK);
	}

	/**
	 * API to get GetstateList Based On countryId
	 * 
	 * @param countryId
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/{countryId}/states")
	@ApiOperation(value = "${ApiOperation.LookupDataController.getAllStateForCountryId}", response = StateLookupDto.class)
	public ResponseEntity<List<StateLookupDto>> getAllStateForCountryId(
			@ApiParam(required = true, name = "countryId", value = "${ApiOperation.LookupDataController.getAllStateForCountryId.countryId}") @PathVariable(value = "countryId", required = true) Long countryId,
			HttpServletRequest request, HttpServletResponse response) {
		List<StateLookupDto> stateLookupDtoList = stateLookupService.findStateList(countryId).stream()
				.map(lookupDataMapper::toStateLookupDto).collect(Collectors.toList());
		LOG.debug("stateList Based on CountryId");
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, String.valueOf(countryId), "Get CountryList Based On countryId",
				Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(stateLookupDtoList, HttpStatus.OK);
	}

	/**
	 * API to Get AllStateAndCountry Details
	 * 
	 * @param countryId
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/CountryStateLookup")
	@ApiOperation(value = "${ApiOperation.LookupDataController.getAllStateAndCountry}", response = CountryStateLookupDto.class)
	public ResponseEntity<List<CountryStateLookupDto>> getAllState(
			@ApiParam(value = "${ApiOperation.LookupDataController.getAllState}") HttpServletRequest request,
			HttpServletResponse response) {
		List<CountryStateLookupDto> stateLookupDtoList = stateLookupService.stateList().stream()
				.map(lookupDataMapper::toCountryStateLookupDto).collect(Collectors.toList());
		LOG.debug("stateList Based on CountryId");
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, null, "Get AllStateAndCountry Details",
				Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(stateLookupDtoList, HttpStatus.OK);
	}

	/**
	 * API to Get ConfigList Based On configtypeCode
	 * 
	 * @param configtypeId
	 * @param request
	 * @param response
	 * @return List Of Configtypes
	 */
	@GetMapping("/configType/{configtypeCode}")
	@ApiOperation(value = "${ApiOperation.LookupDataController.getConfigList}", response = LosConfigDetailsDto.class)
	public ResponseEntity<List<LosConfigDetailsDto>> getConfigTypeEntityList(
			@ApiParam(required = true, name = "configtypeCode", value = "") @PathVariable(value = "configtypeCode", required = true) String configtypeCode,
			HttpServletRequest request, HttpServletResponse response) {
		List<LosConfigDetails> configDetalsList = configLookupService.findConfigDetails(configtypeCode);
		LOG.debug("configList Based on ConfigtypeId");
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, configtypeCode, "Get ConfigList Based On configtypeCode",
				Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(lookupDataMapper.toConfigDetailsDto(configDetalsList), HttpStatus.OK);
	}

	/**
	 * API to Get ConfigList
	 * 
	 * @return List Of Configtypes
	 */
	@GetMapping("/configTypes")
	@ApiOperation(value = "${ApiOperation.LookupDataController.getListOfConfigTypes}", response = LosConfigTypeDto.class)
	public ResponseEntity<List<LosConfigTypeDto>> getListOfConfigTypes(HttpServletRequest request, HttpServletResponse response) {
		List<LosConfigType> configTypeList = configLookupService.findAllConfigTypes();
		LOG.debug("Returning  List Of Configtypes.");
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, null, "Get ConfigList",
				Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(lookupDataMapper.toLosConfigTypeDto(configTypeList), HttpStatus.OK);
	}
}
