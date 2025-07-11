package com.ezee.trip.service.impl;

import com.ezee.trip.service.TripCostService;

import java.util.ArrayList;
import java.util.List;

import com.ezee.trip.dao.TripCostDAO;
import com.ezee.trip.cache.RedisTripService;
import com.ezee.trip.dao.DestinationDAO;
import com.ezee.trip.dao.TaxDAO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.dto.TaxDTO;
import com.ezee.trip.dto.TripCostDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.exception.ErrorCode;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.service.AuthService;
import com.ezee.trip.util.CodeGenarator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripCostImpl implements TripCostService {

	@Autowired
	private AuthService authService;

	@Autowired
	private TripCostDAO tripCostDAO;

	@Autowired
	private RedisTripService tripCache;
	@Autowired
	private DestinationDAO destinationDAO;

	@Autowired
	private TaxDAO taxDAO;

	private static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.impl");

	public List<TripCostDTO> getAllTripCost(String authCode) {
		List<TripCostDTO> list = new ArrayList<>();
		try {
			authService.validateAuthCode(authCode);
			list = tripCostDAO.getAllTripCosts();

			for (TripCostDTO dto : list) {
				TripDTO trip = tripCache.getTripFromCache(dto.getTripDTO());
				DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
				trip.setDestinationDTO(destination);
				dto.setTripDTO(trip);
				if (trip == null || trip.getId() == 0) {
					throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION,
							"Trip not found for trip cost");
				}

				TaxDTO tax = taxDAO.getTax(dto.getTaxDTO());
				dto.setTaxDTO(tax);
				if (tax == null || tax.getId() == 0) {
					throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION, "Tax not found for trip cost");
				}
			}

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error while getting all trip costs: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching trip costs");
		}
		return list;
	}

	public TripCostDTO getTripCostByCode(String code, String authCode) {
		TripCostDTO dto = new TripCostDTO();
		try {
			authService.validateAuthCode(authCode);
			dto.setCode(code);
			dto = tripCostDAO.getTripCost(dto);

			if (dto.getTripDTO() == null || dto.getTripDTO().getId() == 0) {
				throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION, "Trip not found for trip cost");
			}

			if (dto.getTaxDTO() == null || dto.getTaxDTO().getId() == 0) {
				throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION, "Tax not found for trip cost");
			}

			TripDTO trip = tripCache.getTripFromCache(dto.getTripDTO());
			DestinationDTO destination = destinationDAO.getDestination(trip.getDestinationDTO());
			trip.setDestinationDTO(destination);
			dto.setTripDTO(trip);

			TaxDTO tax = taxDAO.getTax(dto.getTaxDTO());
			dto.setTaxDTO(tax);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error while getting trip cost: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching trip cost");
		}
		return dto;
	}

	public void addTripCost(TripCostDTO tripCostDTO, String authCode) {
		try {
			AuthResponseDTO authResponse = authService.validateAuthCode(authCode);
			tripCostDTO.setUpdatedBy(authResponse.getUsername());
			tripCostDTO.setCode(CodeGenarator.generateCode("TPC", 12));

			tripCostDAO.addTripCost(tripCostDTO);
		} catch (Exception e) {
			LOGGER.error("Error while adding/updating trip cost: {}", e.getMessage(), e);
			throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Unexpected error while inserting/updating trip cost");
		}
	}
}
