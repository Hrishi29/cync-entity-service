package com.idexcel.cync.los.entity.service.impl;

import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_NOT_FOUND;
import static com.idexcel.cync.los.entity.common.constants.ErrorMessageConstants.ENTITY_RELATION_EXIST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.common.utils.CommonUtils;
import com.idexcel.cync.los.entity.dao.FinancialRepository;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.exception.ResourceNotFoundException;
import com.idexcel.cync.los.entity.model.ChildEntityNode;
import com.idexcel.cync.los.entity.model.EntityNode;
import com.idexcel.cync.los.entity.model.FinancialEntity;
import com.idexcel.cync.los.entity.model.ParentEntityNode;

@Service
public class EntityRelationshipTreeServiceImpl {

	private final FinancialRepository financialRepository;
	private final MessageSource errorMsgs;

	@Autowired
	public EntityRelationshipTreeServiceImpl(FinancialRepository financialRepository, MessageSource errorMsgs) {
		this.financialRepository = financialRepository;
		this.errorMsgs = errorMsgs;
	}

	public EntityNode createTree(List<EntityNode> nodes, String entityId1) {// NOSONAR
		EntityNode entityNode = new EntityNode(LOSEntityConstants.ROOT_NODE, entityId1, null, findByEntityId(entityId1),
				null);
		nodes.add(entityNode);
		Map<String, EntityNode> mapTmp = new HashMap<>();
		// Save all nodes to a map
		for (EntityNode current : nodes) {
			mapTmp.put(current.getChildId(), current);
		}
		// loop and assign parent/child relationships
		for (EntityNode current : nodes) {
			String parentId = current.getParentId();
			if (parentId != null) {
				EntityNode parent = mapTmp.computeIfAbsent(parentId, k -> mapTmp.get(parentId));
				if (parent != null) {
					current.setParent(parent);
					parent.addChild(current);
					mapTmp.put(parentId, parent);
					mapTmp.put(current.getChildId(), current);
				}
			}
		}
		// get the root
		EntityNode root = null;
		for (EntityNode node : mapTmp.values()) {
			if (node.getParent() == null) {
				root = node;
				break;
			}
		}
		return root;
	}

	public ParentEntityNode createTreeFromParent(List<ParentEntityNode> nodes, String entityId1) {// NOSONAR
		ParentEntityNode entityNode = new ParentEntityNode(LOSEntityConstants.ROOT_NODE, entityId1, null,
				findByEntityId(entityId1), null);
		nodes.add(entityNode);
		HashMap<String, ParentEntityNode> mapTmp = new HashMap<>();
		// Save all nodes to a map
		for (ParentEntityNode current : nodes) {
			mapTmp.put(current.getChildId(), current);
		}
		// loop and assign parent/child relationships
		for (ParentEntityNode current : nodes) {
			String parentId = current.getParentId();
			if (parentId != null) {
				ParentEntityNode parent = mapTmp.computeIfAbsent(parentId, k -> mapTmp.get(parentId));
				if (parent != null) {
					current.setParent(parent);
					parent.addChild(current);
					mapTmp.put(parentId, parent);
					mapTmp.put(current.getChildId(), current);
				}
			}
		}
		// get the root
		ParentEntityNode root = null;
		for (ParentEntityNode node : mapTmp.values()) {
			if (node.getParent() == null) {
				root = node;
				break;
			}
		}
		return root;
	}

//	public ChildEntityNode createTreeFromChild(List<ChildEntityNode> nodes, String entityId1) {// NOSONAR
//		ChildEntityNode entityNode = new ChildEntityNode(LOSEntityConstants.ROOT_NODE, entityId1, null,
//				findByEntityId(entityId1), null);
//		nodes.add(entityNode);
//		Map<String, ChildEntityNode> mapTmp = new HashMap<>();
//		// Save all nodes to a map
//		for (ChildEntityNode current : nodes) {
//			mapTmp.put(current.getChildId(), current);
//		}
//		// loop and assign parent/child relationships
//		for (ChildEntityNode current : nodes) {
//			loopAndAssignParentChildRelationship(current, mapTmp);
//		}
//		// get the root
//		ChildEntityNode root = null;
//		for (ChildEntityNode node : mapTmp.values()) {
//			if (node.getParent() == null) {
//				root = node;
//				break;
//			}
//		}
//		return root;
//	}

	public String findEntityRelationRootNode(List<ChildEntityNode> nodes, String entityId1) {// NOSONAR
		ChildEntityNode entityNode = new ChildEntityNode(LOSEntityConstants.ROOT_NODE, entityId1, null,
				findByEntityId(entityId1), null);
		nodes.add(entityNode);
		Map<String, ChildEntityNode> mapTmp = new HashMap<>();
		// Save all nodes to a map
		for (ChildEntityNode current : nodes) {
			mapTmp.put(current.getChildId(), current);
		}
		// loop and assign parent/child relationships
		String parentRootId = null;
		for (ChildEntityNode current : nodes) {
			loopAndAssignParentChildRelationship(current, mapTmp);

			// get the root
			ChildEntityNode root = null;
			for (ChildEntityNode node : mapTmp.values()) {
				if (node.getParent() != null) {
					root = node;
					if (root.getParentId().equals(entityId1)) {
						parentRootId = root.getChildId();
						break;
					}
				}
			}
		}
		return parentRootId;
	}

	public void validateTree(List<ChildEntityNode> nodes, String entityId1, String entityId2) {
		ChildEntityNode entityNode = new ChildEntityNode("rootNode", entityId1, null, findByEntityId(entityId1), null);
		nodes.add(entityNode);
		Map<String, ChildEntityNode> mapTmp = new HashMap<>();
		// Save all nodes to a map
		for (ChildEntityNode current : nodes) {
			mapTmp.put(current.getChildId(), current);
		}
		// loop and assign parent/child relationships
		for (ChildEntityNode current : nodes) {
			loopAndAssignParentChildRelationship(current, mapTmp);
			// get the root
			ChildEntityNode root = null;
			for (ChildEntityNode node : mapTmp.values()) {
				if (node.getParent() != null) {
					root = node;
					validateRelationExist(root, entityId2);
				}
			}
		}
	}

	private void loopAndAssignParentChildRelationship(ChildEntityNode current, Map<String, ChildEntityNode> mapTmp) {
		String parentId = current.getParentId();
		if (parentId != null) {
			ChildEntityNode parent = mapTmp.computeIfAbsent(parentId, k -> mapTmp.get(parentId));
			if (parent != null) {
				parentPresentSetAndAdd(parentId, current, parent, mapTmp);
			}
		}
	}

	private void parentPresentSetAndAdd(String parentId, ChildEntityNode current, ChildEntityNode parent,
			Map<String, ChildEntityNode> mapTmp) {
		current.setParent(parent);
		parent.addChild(current);
		mapTmp.put(parentId, parent);
		mapTmp.put(current.getChildId(), current);
	}

	private void validateRelationExist(ChildEntityNode root, String entityId2) {
		if (root.getChildId().equals(entityId2)) {
			throw new BadRequestException(errorMsgs.getMessage(ENTITY_RELATION_EXIST, null, CommonUtils.getLocale()));
		}
	}

	public FinancialEntity findByEntityId(String entityId1) {
		return financialRepository.findById(entityId1).orElseThrow(() -> new ResourceNotFoundException(
				errorMsgs.getMessage(ENTITY_NOT_FOUND, new Object[] { entityId1 }, CommonUtils.getLocale())));
	}

}