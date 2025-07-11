package com.ezee.trip.controller.io;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DestinationIO extends BaseIO{
	private String city;
	private String country;
}
