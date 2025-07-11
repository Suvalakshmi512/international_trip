package com.ezee.trip.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripCostDTO extends BaseDTO {
	private TripDTO tripDTO;
	private BigDecimal accomodationCost;
	private BigDecimal travelCost;
	private BigDecimal foodCost;
	private BigDecimal activityCost;
	private BigDecimal serviceFees;
	private TaxDTO taxDTO;
	private String updatedBy;
	private String updatedAt;
}
