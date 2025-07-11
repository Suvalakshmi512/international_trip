package com.ezee.trip.controller.io;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserIO extends BaseIO{
	private String phoneNo;
	private String email;
	private String gender;
	private String dateOfBirth;
	private String address;
}
