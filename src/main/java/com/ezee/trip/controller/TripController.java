package com.ezee.trip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ezee.trip.controller.io.TripIO;
import com.ezee.trip.controller.io.DestinationIO;
import com.ezee.trip.controller.io.TripHighLightIO;
import com.ezee.trip.controller.io.TripIncludeIO;
import com.ezee.trip.controller.io.AvailableDateIO;
import com.ezee.trip.controller.io.ResponseIO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.dto.TripHighLightDTO;
import com.ezee.trip.dto.TripIncludeDTO;
import com.ezee.trip.dto.AvailableDateDTO;
import com.ezee.trip.service.TripService;

@RestController
@RequestMapping("/trip")
public class TripController {

	@Autowired
	private TripService tripService;

	@GetMapping("/")
	public ResponseIO<List<TripIO>> getAllTrip(@RequestHeader("authCode") String authCode) {
		List<TripDTO> allTrips = tripService.getAllTrip(authCode);
		List<TripIO> tripList = new ArrayList<>();
		for (TripDTO tripDTO : allTrips) {
			tripList.add(convertToIO(tripDTO));
		}
		return ResponseIO.success(tripList);
	}

	@GetMapping("/{code}")
	public ResponseIO<TripIO> getTripByCode(@PathVariable("code") String code,
			@RequestHeader("authCode") String authCode) {
		TripDTO tripDTO = tripService.getTrip(code, authCode);
		TripIO tripIO = convertToIO(tripDTO);
		return ResponseIO.success(tripIO);
	}

	@PostMapping("/add")
	public ResponseIO<String> addTrip(@RequestBody TripIO tripIO, @RequestHeader("authCode") String authCode) {
		TripDTO tripDTO = convertToDTO(tripIO);
		tripService.addTrip(tripDTO, authCode);
		return ResponseIO.success("Inserted Successfully");
	}

	private TripIO convertToIO(TripDTO tripDTO) {
		TripIO tripIO = new TripIO();
		tripIO.setCode(tripDTO.getCode());
		tripIO.setName(tripDTO.getName());
		tripIO.setActiveFlag(tripDTO.getActiveFlag());
		tripIO.setTotalSeat(tripDTO.getTotalSeat());
		tripIO.setAvailableSeat(tripDTO.getAvailableSeat());
		tripIO.setDurationDays(tripDTO.getDurationDays());
		tripIO.setPrice(tripDTO.getPrice());
		tripIO.setCurrency(tripDTO.getCurrency());

		
		if (tripDTO.getDestinationDTO() != null) {
			DestinationIO destIO = new DestinationIO();
			destIO.setCode(tripDTO.getDestinationDTO().getCode());
			destIO.setActiveFlag(tripDTO.getDestinationDTO().getActiveFlag());
			destIO.setCity(tripDTO.getDestinationDTO().getCity());
			destIO.setCountry(tripDTO.getDestinationDTO().getCountry());
			tripIO.setDestination(destIO);
		}

		
		if (tripDTO.getTripHighlight() != null) {
			List<TripHighLightIO> highlights = new ArrayList<>();
			for (TripHighLightDTO highlightDTO : tripDTO.getTripHighlight()) {
				TripHighLightIO highlightIO = new TripHighLightIO();
				highlightIO.setCode(highlightDTO.getCode());
				highlightIO.setActiveFlag(highlightDTO.getActiveFlag());
				highlightIO.setHighLight(highlightDTO.getHighLight());
				highlights.add(highlightIO);
			}
			tripIO.setTripHighlight(highlights);
		}

		if (tripDTO.getTripInclude() != null) {
			List<TripIncludeIO> includes = new ArrayList<>();
			for (TripIncludeDTO includeDTO : tripDTO.getTripInclude()) {
				TripIncludeIO includeIO = new TripIncludeIO();
				includeIO.setCode(includeDTO.getCode());
				includeIO.setActiveFlag(includeDTO.getActiveFlag());
				includeIO.setItem(includeDTO.getItem());
				includes.add(includeIO);
			}
			tripIO.setTripInclude(includes);
		}

		if (tripDTO.getAvailableDate() != null) {
			List<AvailableDateIO> dates = new ArrayList<>();
			for (AvailableDateDTO dateDTO : tripDTO.getAvailableDate()) {
				AvailableDateIO dateIO = new AvailableDateIO();
				dateIO.setCode(dateDTO.getCode());
				dateIO.setActiveFlag(dateDTO.getActiveFlag());
				dateIO.setAvailableDate(dateDTO.getAvailableDate());
				dates.add(dateIO);
			}
			tripIO.setAvailableDate(dates);
		}

		return tripIO;
	}

	private TripDTO convertToDTO(TripIO tripIO) {
		TripDTO tripDTO = new TripDTO();
		tripDTO.setCode(tripIO.getCode());
		tripDTO.setName(tripIO.getName());
		tripDTO.setActiveFlag(tripIO.getActiveFlag());
		tripDTO.setTotalSeat(tripIO.getTotalSeat());
		tripDTO.setAvailableSeat(tripIO.getAvailableSeat());
		tripDTO.setDurationDays(tripIO.getDurationDays());
		tripDTO.setPrice(tripIO.getPrice());
		tripDTO.setCurrency(tripIO.getCurrency());

		
		if (tripIO.getDestination() != null) {
			DestinationDTO destDTO = new DestinationDTO();
			destDTO.setCode(tripIO.getDestination().getCode());
			tripDTO.setDestinationDTO(destDTO);
		}

		return tripDTO;
	}
}
