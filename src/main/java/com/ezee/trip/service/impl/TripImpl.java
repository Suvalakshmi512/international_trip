package com.ezee.trip.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.trip.dao.AvailableDateDAO;
import com.ezee.trip.dao.DestinationDAO;
import com.ezee.trip.dao.TripDAO;
import com.ezee.trip.dao.TripHighLightDAO;
import com.ezee.trip.dao.TripIncludeDAO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.dto.AvailableDateDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.TripHighLightDTO;
import com.ezee.trip.dto.TripIncludeDTO;
import com.ezee.trip.exception.ErrorCode;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.service.AuthService;
import com.ezee.trip.service.TripService;
import com.ezee.trip.util.CodeGenarator;
@Service
public class TripImpl implements TripService{
	@Autowired
	private AuthService authService;
	@Autowired
	private TripHighLightDAO tripHighLight;
	@Autowired
	private DestinationDAO destinationDAO;
	@Autowired
	private TripIncludeDAO tripIncludeDAO;
	@Autowired
	private AvailableDateDAO availableDateDAO;
	@Autowired
	private TripDAO tripDAO;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.impl");

	public List<TripDTO> getAllTrip(String authCode) {
		List<TripDTO> allTrip = new ArrayList<TripDTO>();
		List<TripIncludeDTO> tripInclude = new ArrayList<TripIncludeDTO>();
		List<TripHighLightDTO> highLight = new ArrayList<TripHighLightDTO>();
		List<AvailableDateDTO> availableDate = new ArrayList<AvailableDateDTO>();

		try {
			AuthResponseDTO authDTO = authService.validateAuthCode(authCode);
			if (authDTO != null) {
				allTrip = tripDAO.getAllTripDTO();
				for (TripDTO trip : allTrip) {
					if (trip.getDestinationDTO().getId() == 0) {
						throw new ServiceException("Destination ID is 0 for trip: " + trip.getCode());
					}
					DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
					trip.setDestinationDTO(destination);
					highLight = tripHighLight.getAllTripHighLightByTrip(trip);
					trip.setTripHighlight(highLight);
					tripInclude = tripIncludeDAO.getAllTripIncludeByTrip(trip);
					trip.setTripInclude(tripInclude);
					availableDate = availableDateDAO.getAllAvailableDateByTrip(trip);
					trip.setAvailableDate(availableDate);
				}
			}
		} catch (ServiceException se) {
			LOGGER.error("Service exception while getting Dish: {}", se.getMessage(), se);
			throw se;
		} catch (Exception e) {
			LOGGER.error("Error while getting Dish: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching Dish");

		}
		return allTrip;
	}

	public TripDTO getTrip(String code, String authCode) {
		TripDTO tripDTO = new TripDTO();
		List<TripIncludeDTO> tripInclude = new ArrayList<TripIncludeDTO>();
		List<TripHighLightDTO> highLight = new ArrayList<TripHighLightDTO>();
		List<AvailableDateDTO> availableDate = new ArrayList<AvailableDateDTO>();
		try {
			AuthResponseDTO authDTO = authService.validateAuthCode(authCode);
			if (authDTO != null) {
				tripDTO.setCode(code);
				tripDTO = tripDAO.getTrip(tripDTO);
				if (tripDTO.getDestinationDTO().getId() == 0) {
					throw new ServiceException("Destination ID is 0 for trip: " + tripDTO.getCode());
				}
				DestinationDTO destination = destinationDAO.getDestination(tripDTO.getDestinationDTO());
				tripDTO.setDestinationDTO(destination);
				highLight = tripHighLight.getAllTripHighLightByTrip(tripDTO);
				tripDTO.setTripHighlight(highLight);
				tripInclude = tripIncludeDAO.getAllTripIncludeByTrip(tripDTO);
				tripDTO.setTripInclude(tripInclude);
				availableDate = availableDateDAO.getAllAvailableDateByTrip(tripDTO);
				tripDTO.setAvailableDate(availableDate);
			}
		} catch (ServiceException se) {
			LOGGER.error("Service exception while getting Dish: {}", se.getMessage(), se);
			throw se;
		} catch (Exception e) {
			LOGGER.error("Error while getting Dish: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching Dish");

		}
		return tripDTO;
	}
	public void addTrip(TripDTO tripDTO, String authCode) {
        try {
            AuthResponseDTO authResponse = authService.validateAuthCode(authCode);
            tripDTO.setUpdatedBy(authResponse.getUsername());
            tripDTO.setCode(CodeGenarator.generateCode("TRP", 12));
            tripDAO.addTrip(tripDTO);
        } catch (Exception e) {
            LOGGER.error("Error while adding trip highlight: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while inserting trip highlight");
        }
    }
}
