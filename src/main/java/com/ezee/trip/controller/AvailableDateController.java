package com.ezee.trip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ezee.trip.controller.io.AvailableDateIO;
import com.ezee.trip.controller.io.DestinationIO;
import com.ezee.trip.controller.io.TripIO;
import com.ezee.trip.controller.io.ResponseIO;
import com.ezee.trip.dto.AvailableDateDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.service.AvailableDateService;

@RestController
@RequestMapping("/availabledate")
public class AvailableDateController {

	@Autowired
	private AvailableDateService availableDateService;

	@GetMapping("/")
	public ResponseIO<List<AvailableDateIO>> getAllAvailableDate(@RequestHeader("authCode") String authCode) {
		List<AvailableDateDTO> allDate = availableDateService.getAllAvailableDate(authCode);
		List<AvailableDateIO> dateList = new ArrayList<>();
		for (AvailableDateDTO availableDateDTO : allDate) {
			dateList.add(convertToIO(availableDateDTO));
		}
		return ResponseIO.success(dateList);
	}

	@GetMapping("/{code}")
	public ResponseIO<AvailableDateIO> getAvailableDateByCode(@PathVariable("code") String code,
			@RequestHeader("authCode") String authCode) {
		AvailableDateDTO availableDateDTO = availableDateService.getAvailableDateByCode(code, authCode);
		AvailableDateIO availableDateIO = convertToIO(availableDateDTO);
		return ResponseIO.success(availableDateIO);
	}

	@PostMapping("/add")
	public ResponseIO<String> addAvailableDate(@RequestBody AvailableDateIO availableDateIO,
			@RequestHeader("authCode") String authCode) {
		AvailableDateDTO dtoavailableDateDTO = convertToDTO(availableDateIO);
		availableDateService.addAvailableDate(dtoavailableDateDTO, authCode);
		return ResponseIO.success("Inserted Successfully");
	}

	private AvailableDateIO convertToIO(AvailableDateDTO availableDateDTO) {
		AvailableDateIO availableDateIO = new AvailableDateIO();
		availableDateIO.setCode(availableDateDTO.getCode());
		availableDateIO.setName(availableDateDTO.getName());
		availableDateIO.setActiveFlag(availableDateDTO.getActiveFlag());
		availableDateIO.setAvailableDate(availableDateDTO.getAvailableDate());

		if (availableDateDTO.getTripDTO() != null) {
			TripIO tripIO = new TripIO();
			tripIO.setCode(availableDateDTO.getTripDTO().getCode());
			tripIO.setName(availableDateDTO.getTripDTO().getName());
			DestinationIO desti = new DestinationIO();
			desti.setCode(availableDateDTO.getTripDTO().getDestinationDTO().getCode());
			desti.setCity(availableDateDTO.getTripDTO().getDestinationDTO().getCity());
			desti.setCountry(availableDateDTO.getTripDTO().getDestinationDTO().getCountry());
			desti.setActiveFlag(availableDateDTO.getTripDTO().getDestinationDTO().getActiveFlag());
			tripIO.setDestination(desti);
			tripIO.setActiveFlag(availableDateDTO.getTripDTO().getActiveFlag());
			availableDateIO.setTrip(tripIO);
		}
		return availableDateIO;
	}

	private AvailableDateDTO convertToDTO(AvailableDateIO availableDateIO) {
		AvailableDateDTO availableDateDTO = new AvailableDateDTO();
		availableDateDTO.setCode(availableDateIO.getCode());
		availableDateDTO.setName(availableDateIO.getName());
		availableDateDTO.setActiveFlag(availableDateIO.getActiveFlag());
		availableDateDTO.setAvailableDate(availableDateIO.getAvailableDate());

		if (availableDateIO.getTrip() != null) {
			TripDTO tripDTO = new TripDTO();
			tripDTO.setCode(availableDateIO.getTrip().getCode());
			availableDateDTO.setTripDTO(tripDTO);
		}
		return availableDateDTO;
	}
}
