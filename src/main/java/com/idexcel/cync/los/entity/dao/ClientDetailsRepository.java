package com.idexcel.cync.los.entity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idexcel.cync.los.entity.model.ClientEntity;

/**
 * Data Access Object class for {@code ClientDetails}
 * 
 * @author IDEXCEL
 *
 */
@Repository
public interface ClientDetailsRepository extends JpaRepository<ClientEntity, Long> {

	public ClientEntity findByClientIdAndStatus(Long clientId, boolean status);

	public ClientEntity findByClientNameAndStatus(String clientName, boolean status);

}
