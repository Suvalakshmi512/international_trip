package com.ezee.trip.service;

import java.util.List;

import com.ezee.trip.dto.TripIncludeDTO;

public interface TripIncludeService {
	public List<TripIncludeDTO> getAllTripInclude(String authCode);
	public TripIncludeDTO getTripIncludeByCode(String code, String authCode);
	public void addTripInclude(TripIncludeDTO tripIncludeDTO, String authCode);
}
