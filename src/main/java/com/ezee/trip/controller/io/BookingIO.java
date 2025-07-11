package com.ezee.trip.controller.io;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingIO extends BaseIO {
	private UserIO user;
	private TripIO trip;
	private String travelDate;
	private int travelerCount;
	private BigDecimal pricePerPerson;
	private BigDecimal totalPrice;

}
