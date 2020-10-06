package com.idexcel.cync.los.entity.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

/**
 * Class to add auditing for all the finance models
 * 
 * @author Idexcel
 *
 */
public class AuditorAwareImpl implements AuditorAware<String> {

	private static String userId;
	private static String userName;

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of(AuditorAwareImpl.userId);
	}

	public static void setUserId(String userId) {
		AuditorAwareImpl.userId = userId;
	}

	public static String getUserId() {
		return AuditorAwareImpl.userId;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		AuditorAwareImpl.userName = userName;
	}

}