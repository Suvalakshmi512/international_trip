package com.ezee.trip.controller.io;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripHighLightIO extends BaseIO{
	private TripIO trip;
	private String highLight;
}
