package com.idexcel.cync.los.entity.config;

import java.io.Serializable;
import java.util.Properties;

import javax.transaction.Transactional;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import com.idexcel.cync.los.entity.common.utils.CommonUtils;

@Transactional
public class EntitySequenceIdGenerator extends SequenceStyleGenerator {

	public static final String VALUE_PREFIX_PARAMETER = "valuePrefix";
	public static final String VALUE_PREFIX_DEFAULT = "";
	public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
	public static final String NUMBER_FORMAT_DEFAULT = "%d";
	private String numberFormat;
	
	private String valuePrefix;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {
		String clientName = CommonUtils.getClientCodeFromMDC();
		String entityType = CommonUtils.getEntityTypeSuffixFromMDC();
		valuePrefix = clientName;
		String valueSuffix = entityType;

		return valuePrefix + String.format(numberFormat, super.generate(session, object)) + valueSuffix;
	}

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
		super.configure(LongType.INSTANCE, params, serviceRegistry);
		valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER, params, VALUE_PREFIX_DEFAULT);
		numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT);
	}
}
