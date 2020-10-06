package com.idexcel.cync.los.entity.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.ActivityLog;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.common.utils.Operation;
import com.idexcel.cync.los.entity.common.utils.Status;
import com.idexcel.cync.los.entity.dto.CommercialEntityDto;
import com.idexcel.cync.los.entity.dto.FinancialEntityDto;
import com.idexcel.cync.los.entity.dto.FinancialEntityListDto;
import com.idexcel.cync.los.entity.dto.IndividualEntityDto;
import com.idexcel.cync.los.entity.dto.ResponseDto;
import com.idexcel.cync.los.entity.exception.ResourceNotFoundException;
import com.idexcel.cync.los.entity.mapper.FinancialEntityListMapper;
import com.idexcel.cync.los.entity.mapper.FinancialEntityMapper;
import com.idexcel.cync.los.entity.model.ClientEntity;
import com.idexcel.cync.los.entity.model.CommercialEntity;
import com.idexcel.cync.los.entity.model.CommercialEntityList;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.FinancialEntityList;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntity;
import com.idexcel.cync.los.entity.model.IndividualFinancialEntityList;
import com.idexcel.cync.los.entity.model.LosConfigDetails;
import com.idexcel.cync.los.entity.service.DBQueriesService;
import com.idexcel.cync.los.entity.service.FinancialService;
import com.idexcel.cync.los.entity.validator.EntityDtoValidator;
import com.idexcel.cync.los.entity.view.View;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * The class {@code EntityController} represents as a entry point to access the
 * APIs related to Individual Entity and Commercial Entity
 * 
 * @author Idexcel
 */

@RestController
@CrossOrigin // NOSONAR
@RequestMapping(value = CoreConstants.API_BASE_PATH + "/entities")
@Api(tags = { "Entity Controller" }, description = "EntityController : used to Create, Read and Update Entity") // NOSONAR
class EntityController {
	private static final Logger LOG = LoggerFactory.getLogger(EntityController.class);

	private final FinancialService financialService;
	private final FinancialEntityMapper financialEntityMapper;
	private final EntityDtoValidator entityDtoValidator;
	private final ResponseDto responseDto = new ResponseDto();
	private final DBQueriesService dbQueriesService;
	private final MessageSource errorMsgs;
	private final FinancialEntityListMapper financialEntityListMapper;

	/**
	 * Parameterized Constructor which initializes a newly created object
	 * {@code EntityController} and a explicit copy of
	 * {@code FinancialEntityMapper}, {@code FinancialService}, is created
	 * 
	 * @param financialEntityMapper {@code FinancialEntityMapper}
	 * @param financialService      {@code FinancialService}
	 */

	@Autowired
	public EntityController(FinancialService financialService, FinancialEntityMapper financialEntityMapper,
			EntityDtoValidator entityDtoValidator, DBQueriesService dBQueriesService, MessageSource errorMsgs,
			FinancialEntityListMapper financialEntityListMapper) {
		this.financialService = financialService;
		this.financialEntityMapper = financialEntityMapper;
		this.entityDtoValidator = entityDtoValidator;
		this.dbQueriesService = dBQueriesService;
		this.errorMsgs = errorMsgs;
		this.financialEntityListMapper = financialEntityListMapper;
	}

	/**
	 * Method to get the Entity details for particular entityId
	 * 
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @return {@code FinancialEntityDto}
	 */
	@ApiOperation(value = "${ApiOperation.EntityController.getEntityById}")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{entityId}")
	@JsonView(View.AllFields.class)
	public ResponseEntity<FinancialEntityDto> getEntityById(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(required = true, name = "entityId", value = "${ApiOperation.EntityController.getEntityById.entityId}") @PathVariable String entityId) {
		FinancialEntityDto financialEntityDto;
		FinancialEntity financialEntity = financialService.findById(entityId);
		if (financialEntity instanceof IndividualFinancialEntity) {
			financialEntityDto = financialEntityMapper
					.toIndividualEntityDto((IndividualFinancialEntity) financialEntity);
		} else {
			financialEntityDto = financialEntityMapper.toCommercialEntityDto((CommercialEntity) financialEntity);
		}
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, entityId, "Get Entity ById", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(financialEntityDto, HttpStatus.OK);
	}

	/**
	 * API to Get ListOf Entity
	 */
	@ApiOperation(value = "${ApiOperation.EntityController.getListOfEntity}", response = List.class)
	@GetMapping
	public ResponseEntity<List<FinancialEntityListDto>> getEntity(
			@RequestParam(name = "entityTypeId", required = false) Long entityTypeId, HttpServletRequest request,
			HttpServletResponse response) {
		LosConfigDetails losConfigdetails = new LosConfigDetails();
		losConfigdetails.setConfigId(entityTypeId);
		List<FinancialEntityList> financialEntityList = financialService.findAllEntity(losConfigdetails);
		List<FinancialEntityListDto> financialEntityListDto = financialEntityList.stream()
				.map(entityObject -> entityObject instanceof IndividualFinancialEntityList
						? financialEntityListMapper
								.toIndividualEntityListDto((IndividualFinancialEntityList) entityObject)
						: financialEntityListMapper.toCommercialEntityListDto((CommercialEntityList) entityObject))
				.collect(Collectors.toList());
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, null, "Get All Entity List", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(financialEntityListDto, HttpStatus.OK);
	}

	/**
	 * API For Creating Individual Entity
	 * 
	 * @param individualFinancialEntityDto
	 * @param individualEntityTypeId
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityController.createIndividualEntity}", response = IndividualEntityDto.class)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/individual/{individualEntityTypeId}")
	public ResponseEntity<ResponseDto> createIndividualEntity(
			@ApiParam(value = "${ApiOperation.EntityController.createIndividualEntity.individualFinancialEntityDto}") @Validated @RequestBody IndividualEntityDto individualFinancialEntityDto,
			@ApiParam(required = true, name = "individualEntityTypeId", value = "${ApiOperation.EntityController.createIndividualEntity.entityTypeId}") @PathVariable Long individualEntityTypeId,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("create Individual Entity");
		MDC.put(LOSEntityConstants.CLIENT_CODE_KEY, findClientCode());
		MDC.put(LOSEntityConstants.ENTITY_TYPE_KEY, LOSEntityConstants.INDIVIDUAL_SUFFIX_CODE);
		entityDtoValidator.isvalidDate(individualFinancialEntityDto);
		String individualEntityId = financialService.saveEntity(
				financialEntityMapper.toIndividualFinancialEntity(individualFinancialEntityDto),
				individualEntityTypeId);
		LOG.debug("IndividualEntity Created Successfuly with individualEntityId {} / {}", request.getRequestURL(),
				individualEntityId);
		responseDto.setMessage("Individual Entity Created Successfully.");
		responseDto.setId(individualEntityId);
		LOG.info(ActivityLog.getActivityLog(ActivityLog.entityName(individualFinancialEntityDto), Operation.INSERT,
				individualEntityId, "Created IndividualEntity", Status.SUCCESS,
				response.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	/**
	 * API to Create Commercial Entity
	 * 
	 * @param commercialEntityDto
	 * @param commercialEntityTypeId
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityController.createCommercialEntity}", response = CommercialEntityDto.class)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/commercial/{commercialEntityTypeId}")
	public ResponseEntity<ResponseDto> createCommercialEntity(
			@ApiParam(value = "${ApiOperation.EntityController.createCommercialEntity.commercialEntityDto}") @Validated @RequestBody CommercialEntityDto commercialEntityDto,
			@ApiParam(required = true, name = "commercialEntityTypeId", value = "${ApiOperation.EntityController.createCommercialEntity.entityTypeId}") @PathVariable Long commercialEntityTypeId,
			HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Location", "Test");
		LOG.debug("create Business Entity");
		MDC.put(LOSEntityConstants.CLIENT_CODE_KEY, findClientCode());
		MDC.put(LOSEntityConstants.ENTITY_TYPE_KEY, LOSEntityConstants.COMMERCIAL_SUFFIX_CODE);
		entityDtoValidator.isvalidDate(commercialEntityDto);
		entityDtoValidator.isvalidBusinessName(commercialEntityDto);
		String commercialEntityId = financialService
				.saveEntity(financialEntityMapper.toCommercialEntity(commercialEntityDto), commercialEntityTypeId);
		LOG.debug("CommercialEntity Created Successfuly with commercialEntityId {} / {}", request.getRequestURL(),
				commercialEntityId);
		responseDto.setMessage("Commercial Entity Created Successfully.");
		responseDto.setId(commercialEntityId);
		LOG.info(ActivityLog.getActivityLog(commercialEntityDto.getBusinessName(), Operation.INSERT, commercialEntityId,
				"Created CommercialEntity", Status.SUCCESS, response.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	/**
	 * API to Update Individual Entity
	 * 
	 * @param individualEntityDto
	 * @param individualEntityTypeId
	 * @param entityId
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityController.updateIndividualEntity}", response = IndividualEntityDto.class)
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/individual/{individualEntityTypeId}/{entityId}")
	public ResponseEntity<ResponseDto> updateIndividualEntity(
			@ApiParam(value = "${ApiOperation.EntityController.updateIndividualEntity.individualFinancialEntityDto}") @Validated @RequestBody IndividualEntityDto individualEntityDto,
			@ApiParam(required = true, name = "individualEntityTypeId", value = "${ApiOperation.EntityController.updateIndividualEntity.entityTypeId}") @PathVariable Long individualEntityTypeId,
			@ApiParam(required = true, name = "entityId", value = "${ApiOperation.EntityController.updateIndividualEntity.entityId}") @PathVariable String entityId,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("update Individual Entity");
		entityDtoValidator.isvalidDate(individualEntityDto);
		String individualEntityId = financialService.updateEntity(individualEntityTypeId, entityId,
				financialEntityMapper.toIndividualFinancialEntity(individualEntityDto));
		LOG.debug("IndividualEntity Updated Successfuly with individualEntityId {} / {}", request.getRequestURL(),
				individualEntityId);
		responseDto.setMessage("Individual Entity Updated Successfully.");
		responseDto.setId(individualEntityId);
		LOG.info(ActivityLog.getActivityLog(ActivityLog.entityName(individualEntityDto), Operation.UPDATE,
				individualEntityId, "Updated IndividualEntity", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * API to Update Commercial Entity
	 * 
	 * @param commercialEntityDto
	 * @param commercialEntityTypeId
	 * @param entityId
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityController.updateCommercialEntity}", response = CommercialEntityDto.class)
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/commercial/{commercialEntityTypeId}/{entityId}")
	public ResponseEntity<ResponseDto> updateCommercialEntity(
			@ApiParam(value = "${ApiOperation.EntityController.updateCommercialEntity.commercialEntityDto}") @Validated @RequestBody CommercialEntityDto commercialEntityDto,
			@ApiParam(required = true, name = "commercialEntityTypeId", value = "${ApiOperation.EntityController.updateCommercialEntity.entityTypeId}") @PathVariable Long commercialEntityTypeId,
			@ApiParam(required = true, name = "entityId", value = "${ApiOperation.EntityController.updateCommercialEntity.entityId}") @PathVariable String entityId,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("update Commercial Entity");
		entityDtoValidator.isvalidDate(commercialEntityDto);
		entityDtoValidator.isvalidBusinessName(commercialEntityDto);
		String commercialEntityId = financialService.updateEntity(commercialEntityTypeId, entityId,
				financialEntityMapper.toCommercialEntity(commercialEntityDto));
		LOG.debug("Commercial Entity Updated Successfuly {} / {}", request.getRequestURL(), commercialEntityId);
		responseDto.setMessage("Commercial Entity Updated Successfully.");
		responseDto.setId(commercialEntityId);
		LOG.info(ActivityLog.getActivityLog(commercialEntityDto.getBusinessName(), Operation.UPDATE, commercialEntityId,
				"Updated CommercialEntity", Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * API For Searching searchDuplicateEntityName
	 * 
	 * @param entityTypeId
	 * @param businessName
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityController.searchDuplicateEntityName}")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/searchDuplicateEntityName")
	@JsonView(View.AllFields.class)
	public ResponseEntity<List<FinancialEntityDto>> searchDuplicateEntityName(
			@ApiParam(required = true, name = "entityTypeId") @RequestParam(required = true) Long entityTypeId,
			@ApiParam(required = false, name = "businessName") @RequestParam(required = false) String businessName,
			@ApiParam(required = false, name = "firstName") @RequestParam(required = false) String firstName,
			@ApiParam(required = false, name = "middleName") @RequestParam(required = false) String middleName,
			@ApiParam(required = false, name = "lastName") @RequestParam(required = false) String lastName,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("Search Entity by Name");
		List<FinancialEntity> entityList = financialService.searchDuplicateEntityName(entityTypeId, businessName,
				firstName, middleName, lastName);
		List<FinancialEntityDto> financialEntityDtoList = entityList.stream()
				.map(entityObject -> entityObject instanceof IndividualFinancialEntity
						? financialEntityMapper.toIndividualEntityDto((IndividualFinancialEntity) entityObject)
						: financialEntityMapper.toCommercialEntityDto((CommercialEntity) entityObject))
				.collect(Collectors.toList());
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, null, "searchDuplicateEntityName", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(financialEntityDtoList, HttpStatus.OK);
	}

	/**
	 * API For Searching EntityByName
	 * 
	 * @param entityName
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityController.searchEntityByName}")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/nameSearch")
	@JsonView(View.AllFields.class)
	public ResponseEntity<List<FinancialEntityDto>> searchEntityByName(
			@ApiParam(required = true, name = "entityName", value = "${ApiOperation.EntityController.createEntityRelation.entityName}") @RequestParam String entityName,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("Search Entity by Name");
		List<FinancialEntity> entityList = financialService.searchEntityByName(entityName);
		List<FinancialEntityDto> financialEntityDtoList = entityList.stream()
				.map(entityObject -> entityObject instanceof IndividualFinancialEntity
						? financialEntityMapper.toIndividualEntityDto((IndividualFinancialEntity) entityObject)
						: financialEntityMapper.toCommercialEntityDto((CommercialEntity) entityObject))
				.collect(Collectors.toList());
		LOG.info(ActivityLog.getActivityLog(entityName, Operation.GET, null, "Search EntityByName", Status.SUCCESS,
				response.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(financialEntityDtoList, HttpStatus.OK);
	}

	String findClientCode() {
		String lenderName = (String) MDC.get(LOSEntityConstants.CLIENT_NAME_KEY);
		ClientEntity clientInfo = dbQueriesService.fetchClientByName(lenderName);
		if (clientInfo == null) {
			throw new ResourceNotFoundException(errorMsgs.getMessage(ErrorMessageConstants.LENDER_INFO,
					new Object[] { lenderName }, CommonUtils.getLocale()));
		}
		return clientInfo.getClientCode();
	}
}
