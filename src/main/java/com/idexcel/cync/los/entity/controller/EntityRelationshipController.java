package com.idexcel.cync.los.entity.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.ActivityLog;
import com.idexcel.cync.los.entity.common.utils.Operation;
import com.idexcel.cync.los.entity.common.utils.Status;
import com.idexcel.cync.los.entity.dao.ParentEntityNodeRepository;
import com.idexcel.cync.los.entity.dto.EntityNodeDto;
import com.idexcel.cync.los.entity.dto.EntityNodeSpouseDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipBorrowerDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipDto;
import com.idexcel.cync.los.entity.dto.EntityRelationshipTypeDto;
import com.idexcel.cync.los.entity.dto.ResponseDto;
import com.idexcel.cync.los.entity.mapper.EntityRelationshipTypeMapper;
import com.idexcel.cync.los.entity.service.EntityRelationshipTypeService;
import com.idexcel.cync.los.entity.view.View;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin // NOSONAR
@RequestMapping(value = CoreConstants.API_BASE_PATH + "/entities/relations")
@Api(tags = {
		"EntityRelationship Controller" }, description = "EntityRelationshipController : used to Manage EntityRelationship Data of LOS Entity") // NOSONAR
class EntityRelationshipController {

	private static final Logger LOG = LoggerFactory.getLogger(EntityRelationshipController.class);
	private final EntityRelationshipTypeService entityRelationshipTypeService;
	private final EntityRelationshipTypeMapper entityRelationshipTypeMapper;

	@Autowired
	public EntityRelationshipController(EntityRelationshipTypeService entityRelationshipTypeService,
			EntityRelationshipTypeMapper entityRelationshipTypeMapper,
			ParentEntityNodeRepository parentEntityNodeRepository) {
		this.entityRelationshipTypeService = entityRelationshipTypeService;
		this.entityRelationshipTypeMapper = entityRelationshipTypeMapper;
	}

	/**
	 * API For Creating EntityRelationship Based On EntityId.
	 * 
	 * @param entityRelationshipTypeDto
	 * @param entityId
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityRelationshipController.createEntityRelationship}", response = EntityRelationshipDto.class)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{entityId}")
	public ResponseEntity<ResponseDto> createEntityRelation(
			@ApiParam(value = "${ApiOperation.EntityRelationshipController.createEntityRelationshipById.RelationshipDto}") @Validated @RequestBody EntityRelationshipDto entityRelationshipDto,
			@ApiParam(required = true, name = "entityId", value = "${ApiOperation.EntityRelationshipController.createEntityRelationById}") @PathVariable String entityId,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("Create entity relation");
		Long relationId = entityRelationshipTypeService.saveEntityRelation(
				entityRelationshipTypeMapper.toEntityRelationshipCreteType(entityRelationshipDto), entityId);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage("Entity Relation Created Successfully.");
		responseDto.setId(entityId);
		LOG.info(ActivityLog.getActivityLog(null, Operation.INSERT, String.valueOf(relationId),
				"Created EntityRelationship", Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	/**
	 * 
	 * API For Getting EntityRelationshipTree By entityId
	 * 
	 * @param request
	 * @param response
	 * @param entityId
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityRelationshipController.getEntityTreeById}", response = EntityNodeDto.class)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "tree/{entityId}")
	@JsonView(View.EntityTreeDataView.class)
	public ResponseEntity<EntityNodeDto> getEntityTreeById(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(required = true, name = "entityId", value = "${ApiOperation.EntityRelationshipController.getEntityById.entityId}") @PathVariable String entityId) {
		EntityNodeDto entityNodeDto = entityRelationshipTypeMapper
				.toEntityNodeDto(entityRelationshipTypeService.findEntityRelationTreeByParentEntityId(entityId));
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, entityId, "Get EntityRelationshipTree by Entity Id",
				Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(entityNodeDto, HttpStatus.OK);
	}

	/**
	 * 
	 * API For Getting EntityRelationshipTree Spouse By entityId
	 * 
	 * @param request
	 * @param response
	 * @param entityId
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityRelationshipController.getEntityTreeById}", response = EntityNodeDto.class)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "spouse/{entityId}")
	public ResponseEntity<EntityNodeSpouseDto> getEntitySpouse(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(required = true, name = "entityId", value = "${ApiOperation.EntityRelationshipController.getEntityById.entityId}") @PathVariable String entityId) {
		LOG.info("Inside getEntitySpouse with entity ID as ::::::: {}", entityId);
		EntityNodeSpouseDto spouseData = entityRelationshipTypeService.findSpouse(entityId);
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, entityId, "Get Spouse by Entity Id", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(spouseData, HttpStatus.OK);
	}

	/**
	 * API For Getting List Of EntityRelationship By entityId
	 * 
	 * @param request
	 * @param response
	 * @param entityId
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityRelationshipController.getEntityListById}", response = EntityRelationshipTypeDto.class)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{entityId}")
	@JsonView(View.EntityRelationListView.class)
	public ResponseEntity<List<EntityRelationshipTypeDto>> getEntityListById(HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(required = true, name = "entityId", value = "${ApiOperation.EntityRelationshipController.getEntityListById.entityId}") @PathVariable String entityId) {
		List<EntityRelationshipTypeDto> entityRelationshipTypeDto = entityRelationshipTypeMapper
				.toEntityRelationshipTypeDto(entityRelationshipTypeService.findEntityRelationListByEntityId(entityId));
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, entityId, "Get EntityRelationship List by Entity Id",
				Status.SUCCESS, request.getHeader(LOSEntityConstants.TRANSACTION_ID),
				ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(entityRelationshipTypeDto, HttpStatus.OK);
	}

	/**
	 * API For Updating EntityRelationship By entityId
	 * 
	 * @param request
	 * @param response
	 * @param entityRelationshipTypeDto
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityRelationshipController.updateEntityRelationById}", response = EntityRelationshipDto.class)
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
	public ResponseEntity<ResponseDto> updateEntityRelationById(HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(value = "${ApiOperation.EntityRelationshipController.updateEntityRelationById.RelationshipDto}") @Validated @RequestBody EntityRelationshipDto ntityRelationshipDto,
			@ApiParam(required = true, name = "id", value = "${ApiOperation.EntityRelationshipController.updateEntityRelationById.id}") @PathVariable Long id) {
		entityRelationshipTypeService.updateEntityRelaton(id,
				entityRelationshipTypeMapper.toEntityRelationshipCreteType(ntityRelationshipDto));
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage("Entity Relationship Updated Successfully.");
		responseDto.setId(String.valueOf(id));
		LOG.info(ActivityLog.getActivityLog(null, Operation.UPDATE, String.valueOf(id),
				"Updated EntityRelationship by Relation Id", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * API For Delete EntityRelation By RelationId
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityRelationshipController.deleteEntityRelationById}")
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
	public ResponseEntity<ResponseDto> deleteEntityRelationById(HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(required = true, name = "id", value = "${ApiOperation.EntityRelationshipController.deleteEntityRelationById.id}") @PathVariable Long id) {
		entityRelationshipTypeService.deleteEntityRelation(id);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage("Entity Relationship Deleted Successfully.");
		responseDto.setId(String.valueOf(id));
		LOG.info(ActivityLog.getActivityLog(null, Operation.DELETE, String.valueOf(id),
				"Deleted EntityRelationship by Relation Id", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);
	}

	/**
	 * API for Getting RelationshipList for given Borrower's
	 * 
	 * @param request
	 * @param response
	 * @param entityIds
	 * @return
	 */
	@ApiOperation(value = "${ApiOperation.EntityRelationshipController.getBorowersList}", response = EntityRelationshipBorrowerDto.class)
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/borrowers")
	public ResponseEntity<List<EntityRelationshipBorrowerDto>> getEntityRelationsListByIds(HttpServletRequest request,
			HttpServletResponse response,
			@ApiParam(required = true, name = "entityId", value = "${ApiOperation.EntityRelationshipController.getEntityListById.entityId}") @RequestBody List<String> entityIds) {
		List<EntityRelationshipBorrowerDto> entityRelationshipTypeDto = entityRelationshipTypeMapper
				.toEntityRelationshipBorrowerDto(entityRelationshipTypeService.findEntityRelationsListByIds(entityIds));
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET, entityIds.stream().collect(Collectors.joining(",")),
				"Getting RelationshipList for given Borrower's", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(entityRelationshipTypeDto, HttpStatus.OK);
	}

}
