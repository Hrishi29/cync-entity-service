package com.idexcel.cync.los.entity.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.ActivityLog;
import com.idexcel.cync.los.entity.common.utils.Operation;
import com.idexcel.cync.los.entity.common.utils.Status;
import com.idexcel.cync.los.entity.dto.EntityFieldNameValue;
import com.idexcel.cync.los.entity.service.EntityFieldsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * The class {@code EntityFieldsController} represents as a entry point to
 * access the APIs related to Entity Fields.
 * 
 * @author Idexcel
 *
 */
@RestController
@CrossOrigin//NOSONAR
@RequestMapping(value = CoreConstants.API_BASE_PATH)
@Api(tags = {"EntityFields Controller"}, description = "EntityFieldsController : used to Get the fields present In (Commercial Or Individual) Entity")//NOSONAR
public class EntityFieldsController {

	private final EntityFieldsService entityFieldsService;
	
	private static final Logger LOG = LoggerFactory.getLogger(EntityFieldsController.class);

	@Autowired
	public EntityFieldsController(EntityFieldsService entityFieldsService) {
		this.entityFieldsService = entityFieldsService;
	}

	/**
	 * Method to get List of Entity fields required for RiskRating
	 * 
	 * @return
	 */
	@ApiOperation(response = EntityFieldNameValue.class, value = "${ApiOperation.EntityController.entityFields}")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/entityFields")
	public ResponseEntity<List<EntityFieldNameValue>> getAllEntityFields(HttpServletRequest request, HttpServletResponse response) {
		LOG.info(ActivityLog.getActivityLog(null, Operation.GET,
				null, "List Of Entity fields", Status.SUCCESS,
				request.getHeader(LOSEntityConstants.TRANSACTION_ID), ActivityLog.localDateTimeInUTC()));
		return new ResponseEntity<>(entityFieldsService.getEntityFields(), HttpStatus.OK);
	}

}
