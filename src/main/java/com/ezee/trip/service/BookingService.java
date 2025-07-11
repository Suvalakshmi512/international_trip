package com.ezee.trip.service;

import java.util.List;

import com.ezee.trip.dto.BookingDTO;

public interface BookingService {
	
    public void addBooking(BookingDTO bookingDTO, String authCode);
    public BookingDTO getBookingByCode(String code, String authCode);
    public List<BookingDTO> getAllBooking(String authCode);
    


}
