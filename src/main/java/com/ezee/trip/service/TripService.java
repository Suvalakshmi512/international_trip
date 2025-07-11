package com.ezee.trip.service;

import java.util.List;

import com.ezee.trip.dto.TripDTO;

public interface TripService {
	public List<TripDTO> getAllTrip(String authCode);
	public TripDTO getTrip(String code, String authCode);
	public void addTrip(TripDTO tripDTO, String authCode);

}
