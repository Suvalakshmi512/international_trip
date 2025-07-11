package com.ezee.trip.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.trip.cache.RedisTripService;
import com.ezee.trip.dao.DestinationDAO;
import com.ezee.trip.dao.TripHighLightDAO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.TripHighLightDTO;
import com.ezee.trip.exception.ErrorCode;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.service.AuthService;
import com.ezee.trip.service.TripHighLightService;
import com.ezee.trip.util.CodeGenarator;

@Service
public class TripHighLightImpl implements TripHighLightService{

    @Autowired
    private AuthService authService;
    
    @Autowired
    private TripHighLightDAO tripHighlightDAO;
    @Autowired
    private DestinationDAO destinationDAO;
    @Autowired
    private RedisTripService tripCache;

    private static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.impl");

    public List<TripHighLightDTO> getAllTripHighLight(String authCode) {
        List<TripHighLightDTO> list = new ArrayList<>();
        try {
            authService.validateAuthCode(authCode);
            list = tripHighlightDAO.getAllTripHighLight();

            for (TripHighLightDTO data : list) {
                TripDTO trip = tripCache.getTripFromCache(data.getTripDTO());
                DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
                trip.setDestinationDTO(destination);
                data.setTripDTO(trip);
                if (trip == null || trip.getId() == 0) {
                    throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION);
                }
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while getting all trip highlights: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching trip highlights");
        }
        return list;
    }

    public TripHighLightDTO getTripHighLightByCode(String code, String authCode) {
        TripHighLightDTO tripHighlight = new TripHighLightDTO();
        try {
            authService.validateAuthCode(authCode);
            tripHighlight.setCode(code);
            
            tripHighlight = tripHighlightDAO.getTripHighLight(tripHighlight);

            if (tripHighlight.getTripDTO() == null || tripHighlight.getTripDTO().getId() == 0) {
                throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION);
            }

            TripDTO trip = tripCache.getTripFromCache(tripHighlight.getTripDTO());
            DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
            trip.setDestinationDTO(destination);
            tripHighlight.setTripDTO(trip);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while getting trip highlight: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching trip highlight");
        }
        return tripHighlight;
    }

    public void addTripHighLight(TripHighLightDTO tripHighlightDTO, String authCode) {
        try {
            AuthResponseDTO authResponse = authService.validateAuthCode(authCode);
            tripHighlightDTO.setUpdatedBy(authResponse.getUsername());
            tripHighlightDTO.setCode(CodeGenarator.generateCode("THL", 12));

            tripHighlightDAO.addTripHighLight(tripHighlightDTO);
        } catch (Exception e) {
            LOGGER.error("Error while adding trip highlight: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while inserting trip highlight");
        }
    }
}

