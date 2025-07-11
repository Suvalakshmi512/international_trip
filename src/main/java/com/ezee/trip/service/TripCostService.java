package com.ezee.trip.service;

import java.util.List;

import com.ezee.trip.dto.TripCostDTO;

public interface TripCostService {
    public List<TripCostDTO> getAllTripCost(String authCode);
    public TripCostDTO getTripCostByCode(String code, String authCode);
    public void addTripCost(TripCostDTO tripCostDTO, String authCode);


}
