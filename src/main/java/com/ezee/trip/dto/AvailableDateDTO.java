package com.ezee.trip.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AvailableDateDTO extends BaseDTO {
	private TripDTO tripDTO;
	private String availableDate;
	private String updatedBy;
	private String updatedAt;
}
