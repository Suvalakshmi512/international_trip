package com.ezee.trip.service;

import java.util.List;

import com.ezee.trip.dto.AvailableDateDTO;

public interface AvailableDateService {
	public List<AvailableDateDTO> getAllAvailableDate(String authCode);
	public AvailableDateDTO getAvailableDateByCode(String code, String authCode);
	public void addAvailableDate(AvailableDateDTO availableDateDTO, String authCode);
}
