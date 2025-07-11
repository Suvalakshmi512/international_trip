package com.ezee.trip.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezee.trip.cache.RedisTripService;
import com.ezee.trip.dao.DestinationDAO;
import com.ezee.trip.dao.TripIncludeDAO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.TripIncludeDTO;
import com.ezee.trip.exception.ErrorCode;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.service.AuthService;
import com.ezee.trip.service.TripIncludeService;
import com.ezee.trip.util.CodeGenarator;
@Service
public class TripIncludeImpl implements TripIncludeService {
	@Autowired
	private AuthService authService;
	@Autowired
	private TripIncludeDAO tripIncludeDAO;
	@Autowired
	private DestinationDAO destinationDAO;
	@Autowired
	private RedisTripService tripCache;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.impl");

	public List<TripIncludeDTO> getAllTripInclude(String authCode) {
		List<TripIncludeDTO> list = new ArrayList<TripIncludeDTO>();
		try {
			authService.validateAuthCode(authCode);
			list = tripIncludeDAO.getAllTripIncludeDTO();
			for (TripIncludeDTO data : list) {
				TripDTO trip = tripCache.getTripFromCache(data.getTripDTO());
				DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
				trip.setDestinationDTO(destination);
				data.setTripDTO(trip);
				if (data.getTripDTO().getId() == 0) {
					throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION);
				}
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error while getting all tripInclude: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching tripInclude");
		}
		return list;
	}

	public TripIncludeDTO getTripIncludeByCode(String code, String authCode) {
		TripIncludeDTO tripInclude = new TripIncludeDTO();
		try {
			authService.validateAuthCode(authCode);
			tripInclude.setCode(code);

			tripInclude = tripIncludeDAO.getTripInclude(tripInclude);

			if (tripInclude.getTripDTO() == null || tripInclude.getTripDTO().getId() == 0) {
				throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION);
			}

			TripDTO trip = tripCache.getTripFromCache(tripInclude.getTripDTO());
			DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
			trip.setDestinationDTO(destination);
			tripInclude.setTripDTO(trip);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error while getting tripInclude: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching tripInclude");
		}
		return tripInclude;
	}

	public void addTripInclude(TripIncludeDTO tripIncludeDTO, String authCode) {
		try {
			AuthResponseDTO validateAuthCode = authService.validateAuthCode(authCode);
			tripIncludeDTO.setUpdatedBy(validateAuthCode.getUsername());
			tripIncludeDTO.setCode(CodeGenarator.generateCode("TIC", 12));
			tripIncludeDAO.addTripInclude(tripIncludeDTO);
		} catch (Exception e) {
			LOGGER.error("Error while adding tripInclude: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while inserting tripInclude");
		}

	}

}
