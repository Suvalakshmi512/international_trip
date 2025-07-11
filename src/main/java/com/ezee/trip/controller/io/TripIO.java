package com.ezee.trip.controller.io;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class TripIO extends BaseIO{
	private DestinationIO destination;
    private List<TripHighLightIO> tripHighlight;
    private List<TripIncludeIO> tripInclude;
    private List<AvailableDateIO> availableDate;
    private Integer totalSeat;
    private Integer availableSeat;
    private String durationDays;
    private BigDecimal price;
    private String currency;

}
