package com.ezee.trip.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingDTO extends BaseDTO{
	    private UserDTO userDTO;
	    private TripDTO tripDTO;
	    private String travelDate;
	    private int travelerCount;
	    private BigDecimal pricePerPerson;
	    private BigDecimal totalPrice;
	    private String updatedAt;
	    private String updatedBy;
}
