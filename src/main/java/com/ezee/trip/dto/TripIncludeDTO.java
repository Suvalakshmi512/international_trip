package com.ezee.trip.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripIncludeDTO extends BaseDTO {
	private TripDTO tripDTO;
	private String item;
	private String updatedBy;
	private String updatedAt;

}
