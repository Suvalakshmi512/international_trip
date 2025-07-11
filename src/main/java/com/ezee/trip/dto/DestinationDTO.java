package com.ezee.trip.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DestinationDTO extends BaseDTO {
	private String city;
	private String country;
	private String updatedBy;
	private String updatedAt;
}
