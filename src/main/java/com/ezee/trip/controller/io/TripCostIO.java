package com.ezee.trip.controller.io;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class TripCostIO extends BaseIO {
	private TripIO trip;
	private BigDecimal accomodationCost;
	private BigDecimal travelCost;
	private BigDecimal foodCost;
	private BigDecimal activityCost;
	private BigDecimal serviceFees;
	private TaxIO tax;

}
