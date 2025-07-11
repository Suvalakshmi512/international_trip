package com.ezee.trip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ezee.trip.controller.io.TripCostIO;
import com.ezee.trip.controller.io.TripIO;
import com.ezee.trip.controller.io.TaxIO;
import com.ezee.trip.controller.io.DestinationIO;
import com.ezee.trip.controller.io.ResponseIO;
import com.ezee.trip.dto.TaxDTO;
import com.ezee.trip.dto.TripCostDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.service.TripCostService;

@RestController
@RequestMapping("/tripcost")
public class TripCostController {

	@Autowired
	private TripCostService tripCostService;

	@GetMapping("/")
	public ResponseIO<List<TripCostIO>> getAllTripCost(@RequestHeader("authCode") String authCode) {
		List<TripCostDTO> allTripCosts = tripCostService.getAllTripCost(authCode);
		List<TripCostIO> tripCostList = new ArrayList<>();
		for (TripCostDTO tripCostDTO : allTripCosts) {
			tripCostList.add(convertToIO(tripCostDTO));
		}
		return ResponseIO.success(tripCostList);
	}

	@GetMapping("/{code}")
	public ResponseIO<TripCostIO> getTripCostByCode(@PathVariable("code") String code,
			@RequestHeader("authCode") String authCode) {
		TripCostDTO tripCost = tripCostService.getTripCostByCode(code, authCode);
		TripCostIO tripCostIO = convertToIO(tripCost);
		return ResponseIO.success(tripCostIO);
	}

	@PostMapping("/add")
	public ResponseIO<String> addTripCost(@RequestBody TripCostIO tripCostIO,
			@RequestHeader("authCode") String authCode) {
		TripCostDTO tripCostDTO = convertToDTO(tripCostIO);
		tripCostService.addTripCost(tripCostDTO, authCode);
		return ResponseIO.success("Inserted Successfully");
	}

	private TripCostIO convertToIO(TripCostDTO tripCostDTO) {
		TripCostIO tripCostIO = new TripCostIO();
		tripCostIO.setCode(tripCostDTO.getCode());
		tripCostIO.setName(tripCostDTO.getName());
		tripCostIO.setActiveFlag(tripCostDTO.getActiveFlag());
		if (tripCostDTO.getTripDTO() != null) {
			TripIO tripIO = new TripIO();
			tripIO.setCode(tripCostDTO.getTripDTO().getCode());
			tripIO.setName(tripCostDTO.getTripDTO().getName());
			DestinationIO desti = new DestinationIO();
			desti.setCode(tripCostDTO.getTripDTO().getDestinationDTO().getCode());
			desti.setCity(tripCostDTO.getTripDTO().getDestinationDTO().getCity());
			desti.setCountry(tripCostDTO.getTripDTO().getDestinationDTO().getCountry());
			desti.setActiveFlag(tripCostDTO.getTripDTO().getDestinationDTO().getActiveFlag());
			tripIO.setDestination(desti);
			tripIO.setActiveFlag(tripCostDTO.getTripDTO().getActiveFlag());
			tripCostIO.setTrip(tripIO);
		}

		if (tripCostDTO.getTaxDTO() != null) {
			TaxIO taxIO = new TaxIO();
			taxIO.setCode(tripCostDTO.getTaxDTO().getCode());
			taxIO.setDescription(tripCostDTO.getTaxDTO().getDescription());
			taxIO.setRatePercentage(tripCostDTO.getTaxDTO().getRatePercentage());
			tripCostIO.setTax(taxIO);
		}

		tripCostIO.setAccomodationCost(tripCostDTO.getAccomodationCost());
		tripCostIO.setTravelCost(tripCostDTO.getTravelCost());
		tripCostIO.setFoodCost(tripCostDTO.getFoodCost());
		tripCostIO.setActivityCost(tripCostDTO.getActivityCost());
		tripCostIO.setServiceFees(tripCostDTO.getServiceFees());
		return tripCostIO;
	}

	private TripCostDTO convertToDTO(TripCostIO tripCostIO) {
		TripCostDTO tripCostDTO = new TripCostDTO();
		tripCostDTO.setCode(tripCostIO.getCode());
		tripCostDTO.setName(tripCostIO.getName());
		tripCostDTO.setActiveFlag(tripCostIO.getActiveFlag());
		tripCostDTO.setAccomodationCost(tripCostIO.getAccomodationCost());
		tripCostDTO.setTravelCost(tripCostIO.getTravelCost());
		tripCostDTO.setFoodCost(tripCostIO.getFoodCost());
		tripCostDTO.setActivityCost(tripCostIO.getActivityCost());
		tripCostDTO.setServiceFees(tripCostIO.getServiceFees());

		if (tripCostIO.getTrip() != null) {
			TripDTO tripDTO = new TripDTO();
			tripDTO.setCode(tripCostIO.getTrip().getCode());
			tripCostDTO.setTripDTO(tripDTO);
		}

		if (tripCostIO.getTax() != null) {
			TaxDTO taxDTO = new TaxDTO();
			taxDTO.setCode(tripCostIO.getTax().getCode());
			tripCostDTO.setTaxDTO(taxDTO);
		}

		return tripCostDTO;
	}

}
