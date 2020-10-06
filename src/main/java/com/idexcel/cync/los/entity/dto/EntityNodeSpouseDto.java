/**
 * 
 */
package com.idexcel.cync.los.entity.dto;

import lombok.Data;

/**
 * @author koushik Mullick
 *
 */

@Data
public class EntityNodeSpouseDto {
	private String spouseEntityID;
	private String firstName;
	private String lastName;

	public EntityNodeSpouseDto() {
		this.spouseEntityID = "";
		this.firstName = "";
		this.lastName = "";
	}

}
