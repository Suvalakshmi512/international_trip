package com.ezee.trip.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ezee.trip.controller.io.TripIncludeIO;
import com.ezee.trip.controller.io.TripIO;
import com.ezee.trip.controller.io.DestinationIO;
import com.ezee.trip.controller.io.ResponseIO;
import com.ezee.trip.dto.TripIncludeDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.service.TripIncludeService;

@RestController
@RequestMapping("/tripinclude")
public class TripIncludeController {

    @Autowired
    private TripIncludeService tripIncludeService;

    @GetMapping("/")
    public ResponseIO<List<TripIncludeIO>> getAllTripInclude(@RequestHeader("authCode") String authCode) {
        List<TripIncludeDTO> allIncludes = tripIncludeService.getAllTripInclude(authCode);
        List<TripIncludeIO> includeList = new ArrayList<>();
        for (TripIncludeDTO tripIncludeDTO : allIncludes) {
            includeList.add(convertToIO(tripIncludeDTO));
        }
        return ResponseIO.success(includeList);
    }

    @GetMapping("/{code}")
    public ResponseIO<TripIncludeIO> getTripIncludeByCode(@PathVariable("code") String code,
                                                          @RequestHeader("authCode") String authCode) {
        TripIncludeDTO tripIncludeDTO = tripIncludeService.getTripIncludeByCode(code, authCode);
        TripIncludeIO tripIncludeIO = convertToIO(tripIncludeDTO);
        return ResponseIO.success(tripIncludeIO);
    }

    @PostMapping("/add")
    public ResponseIO<String> addTripInclude(@RequestBody TripIncludeIO tripIncludeIO,
                                             @RequestHeader("authCode") String authCode) {
        TripIncludeDTO tripIncludeDTO = convertToDTO(tripIncludeIO);
        tripIncludeService.addTripInclude(tripIncludeDTO, authCode);
        return ResponseIO.success("Inserted Successfully");
    }

    private TripIncludeIO convertToIO(TripIncludeDTO tripIncludeDTO) {
        TripIncludeIO tripIncludeIO = new TripIncludeIO();
        tripIncludeIO.setCode(tripIncludeDTO.getCode());
        tripIncludeIO.setName(tripIncludeDTO.getName());
        tripIncludeIO.setActiveFlag(tripIncludeDTO.getActiveFlag());
        tripIncludeIO.setItem(tripIncludeDTO.getItem());

        if (tripIncludeDTO.getTripDTO() != null) {
        	TripIO tripIO = new TripIO();
            tripIO.setCode(tripIncludeDTO.getTripDTO().getCode());
            tripIO.setName(tripIncludeDTO.getTripDTO().getName());
            DestinationIO desti = new DestinationIO();
            desti.setCode(tripIncludeDTO.getTripDTO().getDestinationDTO().getCode());
            desti.setCity(tripIncludeDTO.getTripDTO().getDestinationDTO().getCity());
            desti.setCountry(tripIncludeDTO.getTripDTO().getDestinationDTO().getCountry());
            desti.setActiveFlag(tripIncludeDTO.getTripDTO().getDestinationDTO().getActiveFlag());
            tripIO.setDestination(desti);
            tripIO.setActiveFlag(tripIncludeDTO.getTripDTO().getActiveFlag());
            tripIncludeIO.setTrip(tripIO);
        }
        return tripIncludeIO;
    }

    private TripIncludeDTO convertToDTO(TripIncludeIO tripIncludeIO) {
        TripIncludeDTO tripIncludeDTO = new TripIncludeDTO();
        tripIncludeDTO.setCode(tripIncludeIO.getCode());
        tripIncludeDTO.setName(tripIncludeIO.getName());
        tripIncludeDTO.setActiveFlag(tripIncludeIO.getActiveFlag());
        tripIncludeDTO.setItem(tripIncludeIO.getItem());

        if (tripIncludeIO.getTrip() != null) {
            TripDTO tripDTO = new TripDTO();
            tripDTO.setCode(tripIncludeIO.getTrip().getCode());
            tripIncludeDTO.setTripDTO(tripDTO);
        }
        return tripIncludeDTO;
    }
}

