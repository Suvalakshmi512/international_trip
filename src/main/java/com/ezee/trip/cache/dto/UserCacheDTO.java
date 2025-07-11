package com.ezee.trip.cache.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserCacheDTO implements Serializable{
	private static final long serialVersionUID = 7643540122205147224L;
	private int id;
	private String name;
	private String code;
	private String phoneNo;
	private String email;
	private String gender;
	private String dateOfBirth;
	private String address;
	private int activeFlag;
	private String updatedBy;
	private String updatedAt;

}
