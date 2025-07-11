package com.ezee.trip.service;

import java.util.List;

import com.ezee.trip.dto.DestinationDTO;

public interface DestinationService {
    public void addDestination(DestinationDTO destinationDTO, String authCode) ;
    public DestinationDTO getDestinationByCode(String code, String authCode);
    public List<DestinationDTO> getAllDestination(String authCode);
	

}
