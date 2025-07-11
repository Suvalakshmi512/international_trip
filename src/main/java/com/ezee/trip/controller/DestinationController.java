package com.ezee.trip.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ezee.trip.controller.io.DestinationIO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.service.DestinationService;
import com.ezee.trip.controller.io.ResponseIO;

@RestController
@RequestMapping("/destination")
public class DestinationController {

	@Autowired
	private DestinationService destinationService;

	@GetMapping("/")
	public ResponseIO<List<DestinationIO>> getAllDestination(@RequestHeader("authCode") String authCode) {
		List<DestinationDTO> allDestination = destinationService.getAllDestination(authCode);
		List<DestinationIO> destinationList = new ArrayList<>();
		for (DestinationDTO destinationDTO : allDestination) {
			DestinationIO destinationIO = convertToIO(destinationDTO);
			destinationList.add(destinationIO);
		}
		return ResponseIO.success(destinationList);
	}

	@GetMapping("/{code}")
	public ResponseIO<DestinationIO> getDestinationByCode(@PathVariable("code") String code,
			@RequestHeader("authCode") String authCode) {
		DestinationDTO destinationDTO = destinationService.getDestinationByCode(code, authCode);
		DestinationIO destinationIO = convertToIO(destinationDTO);
		return ResponseIO.success(destinationIO);
	}

	@PostMapping("/add")
	public ResponseIO<String> addDestination(@RequestBody DestinationIO destinationIO,
			@RequestHeader("authCode") String authCode) {
		DestinationDTO dto = convertToDTO(destinationIO);
		destinationService.addDestination(dto, authCode);
		return ResponseIO.success("Inserted Successfully");
	}

//    @PostMapping("/{code}/update")
//    public ResponseIO<String> updateDestination(@PathVariable("code") String code,
//                                                @RequestBody Map<String, Object> updates,
//                                                @RequestHeader("authCode") String authCode) {
//        DestinationDTO dto = new DestinationDTO();
//        dto.setCode(code);
//        destinationService.updateDestination(updates, dto, authCode);
//        return ResponseIO.success("Updated Successfully");
//    }

	private DestinationIO convertToIO(DestinationDTO destinationDTO) {
		DestinationIO destinationIO = new DestinationIO();
		destinationIO.setCode(destinationDTO.getCode());
		destinationIO.setCity(destinationDTO.getCity());
		destinationIO.setCountry(destinationDTO.getCountry());
		destinationIO.setActiveFlag(destinationDTO.getActiveFlag());
		return destinationIO;
	}

	private DestinationDTO convertToDTO(DestinationIO destinationIO) {
		DestinationDTO destinationDTO = new DestinationDTO();
		destinationDTO.setCode(destinationIO.getCode());
		destinationDTO.setActiveFlag(destinationIO.getActiveFlag());
		destinationDTO.setCity(destinationIO.getCity());
		destinationDTO.setCountry(destinationIO.getCountry());
		return destinationDTO;
	}
}
