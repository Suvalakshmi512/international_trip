package com.ezee.trip.service.impl;

import java.util.List;

import com.ezee.trip.cache.RedisTripService;
import com.ezee.trip.cache.RedisUserService;
import com.ezee.trip.dao.BookingDAO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.dto.BookingDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.UserDTO;
import com.ezee.trip.exception.ErrorCode;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.service.AuthService;
import com.ezee.trip.service.BookingService;
import com.ezee.trip.util.CodeGenarator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingImpl implements BookingService {

    @Autowired
    private AuthService authService;

    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    private RedisTripService tripCache;

    @Autowired
    private RedisUserService userCache;

    private static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.impl");

    public void addBooking(BookingDTO bookingDTO, String authCode) {
        try {
            AuthResponseDTO authResponse = authService.validateAuthCode(authCode);
            bookingDTO.setUpdatedBy(authResponse.getUsername());
            bookingDTO.setCode(CodeGenarator.generateCode("BOK", 12));

            bookingDAO.addOrUpdateBooking(bookingDTO);
        } catch (Exception e) {
            LOGGER.error("Error while adding booking: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while adding booking");
        }
    }

    public BookingDTO getBookingByCode(String code, String authCode) {
        BookingDTO booking = new BookingDTO();
        try {
            authService.validateAuthCode(authCode);
            booking.setCode(code);
            booking = bookingDAO.getBooking(booking);

            if (booking.getId() == 0) {
                throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION, "Booking not found");
            }

            TripDTO trip = tripCache.getTripFromCache(booking.getTripDTO());
            booking.setTripDTO(trip);

            UserDTO user = userCache.getUserFromCache(booking.getUserDTO());
            booking.setUserDTO(user);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while fetching booking by code: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching booking");
        }
        return booking;
    }


    public List<BookingDTO> getAllBooking(String authCode) {
        try {
            authService.validateAuthCode(authCode);
            List<BookingDTO> list = bookingDAO.getAllBookings();

            for (BookingDTO booking : list) {
                TripDTO trip = tripCache.getTripFromCache(booking.getTripDTO());
                booking.setTripDTO(trip);

                UserDTO user = userCache.getUserFromCache(booking.getUserDTO());
                booking.setUserDTO(user);
            }

            return list;
        } catch (Exception e) {
            LOGGER.error("Error while fetching all bookings: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching bookings");
        }
    }
}
