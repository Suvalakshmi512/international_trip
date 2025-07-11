package com.ezee.trip.controller.io;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripIncludeIO extends BaseIO{
	private TripIO trip;
	private String item;
}
