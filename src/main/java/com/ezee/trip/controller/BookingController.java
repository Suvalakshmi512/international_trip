package com.ezee.trip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ezee.trip.controller.io.AvailableDateIO;
import com.ezee.trip.controller.io.BookingIO;
import com.ezee.trip.controller.io.DestinationIO;
import com.ezee.trip.controller.io.ResponseIO;
import com.ezee.trip.controller.io.TripHighLightIO;
import com.ezee.trip.controller.io.TripIO;
import com.ezee.trip.controller.io.TripIncludeIO;
import com.ezee.trip.controller.io.UserIO;
import com.ezee.trip.dto.AvailableDateDTO;
import com.ezee.trip.dto.BookingDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.TripHighLightDTO;
import com.ezee.trip.dto.TripIncludeDTO;
import com.ezee.trip.dto.UserDTO;
import com.ezee.trip.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {
	@Autowired
	private BookingService booking;

	@GetMapping("/")
	public ResponseIO<List<BookingIO>> getAllBooking(@RequestHeader("authCode") String authCode) {
		List<BookingDTO> allBooking = booking.getAllBooking(authCode);
		List<BookingIO> bookList = new ArrayList<>();
		for (BookingDTO bookingDTO : allBooking) {
			bookList.add(convertToIO(bookingDTO));
		}
		return ResponseIO.success(bookList);
	}

	@GetMapping("/{code}")
	public ResponseIO<BookingIO> getBooking(@PathVariable("code") String code,
			@RequestHeader("authCode") String authCode) {
		BookingDTO allBooking = booking.getBookingByCode(code, authCode);
		BookingIO booking = convertToIO(allBooking);
		return ResponseIO.success(booking);
	}

	@PostMapping("/add")
	public ResponseIO<String> addBooking(@RequestBody BookingIO bookingIO, @RequestHeader("authCode") String authCode) {
		BookingDTO bookingDTO = convertToDTO(bookingIO);
		booking.addBooking(bookingDTO, authCode);
		return ResponseIO.success("Inserted Successfully");
	}

	private BookingDTO convertToDTO(BookingIO bookingIO) {
		BookingDTO booking = new BookingDTO();
		booking.setTravelDate(bookingIO.getTravelDate());
		booking.setTravelerCount(bookingIO.getTravelerCount());

		if (bookingIO.getUser() != null) {
			UserDTO user = new UserDTO();
			user.setCode(bookingIO.getUser().getCode());
			booking.setUserDTO(user);
		}
		if (bookingIO.getTrip() != null) {
			TripDTO trip = new TripDTO();
			trip.setCode(bookingIO.getTrip().getCode());
			booking.setTripDTO(trip);
		}
		booking.setActiveFlag(bookingIO.getActiveFlag());
		return booking;
	}

	private BookingIO convertToIO(BookingDTO bookingDTO) {
		BookingIO bookingIO = new BookingIO();
		bookingIO.setCode(bookingDTO.getCode());
		bookingIO.setTravelDate(bookingDTO.getTravelDate());
		bookingIO.setTravelerCount(bookingDTO.getTravelerCount());
		if (bookingDTO.getTripDTO() != null) {
			bookingIO.setTrip(convertToIO(bookingDTO.getTripDTO()));
		}
		if (bookingDTO.getUserDTO() != null) {
			UserIO user = new UserIO();
			user.setCode(bookingDTO.getUserDTO().getCode());
			user.setName(bookingDTO.getUserDTO().getName());
			user.setEmail(bookingDTO.getUserDTO().getEmail());
			user.setPhoneNo(bookingDTO.getUserDTO().getPhoneNo());
			user.setDateOfBirth(bookingDTO.getUserDTO().getDateOfBirth());
			user.setAddress(bookingDTO.getUserDTO().getAddress());
			user.setActiveFlag(bookingDTO.getUserDTO().getActiveFlag());
			bookingIO.setUser(user);
		}
		return bookingIO;
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

}
