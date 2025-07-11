package com.ezee.trip.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO{
	private String phoneNo;
	private String email;
	private String gender;
	private String dateOfBirth;
	private String address;
	private String updatedBy;
	private String updatedAt;
}
