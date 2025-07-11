package com.ezee.trip.controller.io;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class AvailableDateIO extends BaseIO {
	private TripIO trip;
	private String availableDate;

}
