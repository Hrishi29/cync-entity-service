package com.idexcel.cync.los.entity.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;


@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

	private static final String DEFAULT_TENANT_ID = "tenant_1";

	@Override
	public String resolveCurrentTenantIdentifier() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			String identifier = (String) requestAttributes.getAttribute(CoreConstants.CURRENT_TENANT,
					RequestAttributes.SCOPE_REQUEST);
			if (identifier != null) {
				return identifier;
			}
		}
		return DEFAULT_TENANT_ID;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}
