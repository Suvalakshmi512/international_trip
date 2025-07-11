package com.ezee.trip.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ezee.trip.dao.DestinationDAO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.exception.ErrorCode;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.service.AuthService;
import com.ezee.trip.service.DestinationService;
import com.ezee.trip.util.CodeGenarator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DestinationImpl implements DestinationService {

	@Autowired
	private AuthService authService;

	@Autowired
	private DestinationDAO destinationDAO;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.impl");

	public List<DestinationDTO> getAllDestination(String authCode) {
		List<DestinationDTO> list = new ArrayList<>();
		try {
			authService.validateAuthCode(authCode);
			list = destinationDAO.getAllDestinations();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error while getting all destinations: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching destinations");
		}
		return list;
	}

	public DestinationDTO getDestinationByCode(String code, String authCode) {
		DestinationDTO destinationDTO = new DestinationDTO();
		try {
			authService.validateAuthCode(authCode);
			destinationDTO.setCode(code);
			destinationDTO = destinationDAO.getDestination(destinationDTO);

			if (destinationDTO.getId() == 0) {
				throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION);
			}

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error while getting destination: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching destination");
		}
		return destinationDTO;
	}

	public void addDestination(DestinationDTO destinationDTO, String authCode) {
		try {
			AuthResponseDTO authResponse = authService.validateAuthCode(authCode);
			destinationDTO.setUpdatedBy(authResponse.getUsername());
			destinationDTO.setCode(CodeGenarator.generateCode("DST", 12));

			destinationDAO.addDestination(destinationDTO);
		} catch (Exception e) {
			LOGGER.error("Error while adding destination: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while inserting destination");
		}
	}

}
