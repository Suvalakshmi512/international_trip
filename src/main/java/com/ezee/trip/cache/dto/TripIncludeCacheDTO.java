package com.ezee.trip.cache.dto;

import java.io.Serializable;

import com.ezee.trip.dto.TripDTO;

import lombok.Data;
@Data
public class TripIncludeCacheDTO implements Serializable{
	private static final long serialVersionUID = 7643540122205147224L;
	private int id;
	private String code;
	private TripDTO tripDTO;
	private String item;
	private int activeFlag;
	private String updatedBy;
	private String updatedAt;

}
