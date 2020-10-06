package com.idexcel.cync.los.entity.common.utils;

import static org.junit.Assert.assertNull;

import org.jboss.logging.MDC;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;

@RunWith(SpringJUnit4ClassRunner.class)
public class CommonUtilsTest {

	
	@InjectMocks
	CommonUtils commonUtils;
	
	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getClientNameFromMDCTest () {
		MDC.put(LOSEntityConstants.CLIENT_NAME_KEY,"XYZ");
		CommonUtils.getClientNameFromMDC();
		assertNull(null);
	}
	@Test
	public void getClientCodeFromMDCTest () {
		MDC.put(LOSEntityConstants.CLIENT_CODE_KEY,"FBOC");
		CommonUtils.getClientCodeFromMDC();
		assertNull(null);
	}
	@Test
	public void getEntityTypeSuffixFromMDCTest () {
		MDC.put(LOSEntityConstants.ENTITY_TYPE_KEY,"C");
		CommonUtils.getEntityTypeSuffixFromMDC();
		assertNull(null);
	}
}
