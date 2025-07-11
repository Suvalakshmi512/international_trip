package com.ezee.trip.service;

import java.util.List;

import com.ezee.trip.dto.TripHighLightDTO;

public interface TripHighLightService {
    public List<TripHighLightDTO> getAllTripHighLight(String authCode);
    public TripHighLightDTO getTripHighLightByCode(String code, String authCode);
    public void addTripHighLight(TripHighLightDTO tripHighlightDTO, String authCode);
}
