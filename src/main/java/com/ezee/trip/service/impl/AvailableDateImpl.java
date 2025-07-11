package com.ezee.trip.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ezee.trip.dao.AvailableDateDAO;
import com.ezee.trip.dao.DestinationDAO;
import com.ezee.trip.dao.TripDAO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.dto.AvailableDateDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.exception.ErrorCode;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.service.AuthService;
import com.ezee.trip.service.AvailableDateService;
import com.ezee.trip.util.CodeGenarator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailableDateImpl implements AvailableDateService {

	@Autowired
	private AuthService authService;

	@Autowired
	private AvailableDateDAO availableDateDAO;
	@Autowired
	private DestinationDAO destinationDAO;

	@Autowired
	private TripDAO tripDAO;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.impl");

	public List<AvailableDateDTO> getAllAvailableDate(String authCode) {
		List<AvailableDateDTO> list = new ArrayList<>();
		try {
			authService.validateAuthCode(authCode);
			list = availableDateDAO.getAllAvailableDate();

			for (AvailableDateDTO dto : list) {
				TripDTO trip = tripDAO.getTrip(dto.getTripDTO());
				DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
				trip.setDestinationDTO(destination);
				dto.setTripDTO(trip);
				if (trip == null || trip.getId() == 0) {
					throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION);
				}
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error while getting all available dates: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Unexpected error while fetching available dates");
		}
		return list;
	}

	public AvailableDateDTO getAvailableDateByCode(String code, String authCode) {
		AvailableDateDTO dto = new AvailableDateDTO();
		try {
			authService.validateAuthCode(authCode);
			dto.setCode(code);
			dto = availableDateDAO.getAvailableDate(dto);

			if (dto.getTripDTO() == null || dto.getTripDTO().getId() == 0) {
				throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION);
			}

			TripDTO trip = tripDAO.getTrip(dto.getTripDTO());
			DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
			trip.setDestinationDTO(destination);
			dto.setTripDTO(trip);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error while getting available date: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Unexpected error while fetching available date");
		}
		return dto;
	}

	public void addAvailableDate(AvailableDateDTO availableDateDTO, String authCode) {
		try {
			AuthResponseDTO authResponse = authService.validateAuthCode(authCode);
			availableDateDTO.setUpdatedBy(authResponse.getUsername());
			availableDateDTO.setCode(CodeGenarator.generateCode("AVD", 12));

			availableDateDAO.addAvailableDate(availableDateDTO);
		} catch (Exception e) {
			LOGGER.error("Error while adding available date: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Unexpected error while inserting available date");
		}
	}
}
