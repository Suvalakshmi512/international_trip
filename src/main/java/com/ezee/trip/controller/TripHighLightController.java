package com.ezee.trip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ezee.trip.controller.io.TripHighLightIO;
import com.ezee.trip.controller.io.TripIO;
import com.ezee.trip.controller.io.DestinationIO;
import com.ezee.trip.controller.io.ResponseIO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.TripHighLightDTO;
import com.ezee.trip.service.TripHighLightService;

@RestController
@RequestMapping("/triphighlight")
public class TripHighLightController {

	@Autowired
	private TripHighLightService tripHighLightService;

	@GetMapping("/")
	public ResponseIO<List<TripHighLightIO>> getAllTripHighlight(@RequestHeader("authCode") String authCode) {
		List<TripHighLightDTO> allHighlights = tripHighLightService.getAllTripHighLight(authCode);
		List<TripHighLightIO> highlightList = new ArrayList<>();
		for (TripHighLightDTO tripHighLightDTO : allHighlights) {
			highlightList.add(convertToIO(tripHighLightDTO));
		}
		return ResponseIO.success(highlightList);
	}

	@GetMapping("/{code}")
	public ResponseIO<TripHighLightIO> getTripHighlightByCode(@PathVariable("code") String code,
			@RequestHeader("authCode") String authCode) {
		TripHighLightDTO tripHighLightDTO = tripHighLightService.getTripHighLightByCode(code, authCode);
		TripHighLightIO tripHighLightIO = convertToIO(tripHighLightDTO);
		return ResponseIO.success(tripHighLightIO);
	}

	@PostMapping("/add")
	public ResponseIO<String> addTripHighlight(@RequestBody TripHighLightIO tripHighLightIO,
			@RequestHeader("authCode") String authCode) {
		TripHighLightDTO dto = convertToDTO(tripHighLightIO);
		tripHighLightService.addTripHighLight(dto, authCode);
		return ResponseIO.success("Inserted Successfully");
	}

	private TripHighLightIO convertToIO(TripHighLightDTO tripHighLightDTO) {
		TripHighLightIO tripHighLightIO = new TripHighLightIO();
		tripHighLightIO.setCode(tripHighLightDTO.getCode());
		tripHighLightIO.setName(tripHighLightDTO.getName());
		tripHighLightIO.setActiveFlag(tripHighLightDTO.getActiveFlag());
		tripHighLightIO.setHighLight(tripHighLightDTO.getHighLight());

		if (tripHighLightDTO.getTripDTO() != null) {
			TripIO tripIO = new TripIO();
			tripIO.setCode(tripHighLightDTO.getTripDTO().getCode());
			tripIO.setName(tripHighLightDTO.getTripDTO().getName());
			DestinationIO desti = new DestinationIO();
			desti.setCode(tripHighLightDTO.getTripDTO().getDestinationDTO().getCode());
			desti.setCity(tripHighLightDTO.getTripDTO().getDestinationDTO().getCity());
			desti.setCountry(tripHighLightDTO.getTripDTO().getDestinationDTO().getCountry());
			desti.setActiveFlag(tripHighLightDTO.getTripDTO().getDestinationDTO().getActiveFlag());
			tripIO.setDestination(desti);
			tripIO.setActiveFlag(tripHighLightDTO.getTripDTO().getActiveFlag());
			tripHighLightIO.setTrip(tripIO);
		}
		return tripHighLightIO;
	}

	private TripHighLightDTO convertToDTO(TripHighLightIO tripHighLightIO) {
		TripHighLightDTO tripHighLightDTO = new TripHighLightDTO();
		tripHighLightDTO.setCode(tripHighLightIO.getCode());
		tripHighLightDTO.setName(tripHighLightIO.getName());
		tripHighLightDTO.setActiveFlag(tripHighLightIO.getActiveFlag());
		tripHighLightDTO.setHighLight(tripHighLightIO.getHighLight());

		if (tripHighLightIO.getTrip() != null) {
			TripDTO tripDTO = new TripDTO();
			tripDTO.setCode(tripHighLightIO.getTrip().getCode());
			tripHighLightDTO.setTripDTO(tripDTO);
		}
		return tripHighLightDTO;
	}
}
