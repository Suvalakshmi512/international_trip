package com.ezee.trip.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TripDTO extends BaseDTO {
	private DestinationDTO destinationDTO;
	private List<TripHighLightDTO> tripHighlight;
	private List<TripIncludeDTO> tripInclude;
	private List<AvailableDateDTO> availableDate;
	private int totalSeat;
	private int availableSeat;
	private String durationDays;
	private BigDecimal price;
	private String currency;
	private String updatedBy;
	private String updatedAt;
}
