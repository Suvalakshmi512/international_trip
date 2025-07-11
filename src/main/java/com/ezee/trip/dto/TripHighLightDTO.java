package com.ezee.trip.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripHighLightDTO extends BaseDTO{
	private TripDTO tripDTO;
	private String highLight;
	private String updatedBy;
	private String updatedAt;
}
