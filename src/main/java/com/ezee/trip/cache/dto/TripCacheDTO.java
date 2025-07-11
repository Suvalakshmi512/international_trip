package com.ezee.trip.cache.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.ezee.trip.dto.AvailableDateDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.dto.TripHighLightDTO;
import com.ezee.trip.dto.TripIncludeDTO;

import lombok.Data;

@Data
public class TripCacheDTO implements Serializable{
	private static final long serialVersionUID = 7643540122205147224L;
	private int id;
	private String name;
	private String code;
	private DestinationDTO destinationDTO;
	private List<TripHighLightDTO> tripHighlight;
	private List<TripIncludeDTO> tripInclude;
	private List<AvailableDateDTO> availableDate;
	private int totalSeat;
	private int availableSeat;
	private String durationDays;
	private BigDecimal price;
	private String currency;
	private int activeFlag;
	private String updatedBy;
	private String updatedAt;
}
