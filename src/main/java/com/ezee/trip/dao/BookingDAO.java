package com.ezee.trip.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import com.ezee.trip.dto.BookingDTO;
import com.ezee.trip.dto.UserDTO;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.dto.TripDTO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Cleanup;

@Repository
public class BookingDAO {

	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.dao");

	public int addOrUpdateBooking(BookingDTO booking) {
		int affected = 0;
		try {
			int pindex = 0;
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			CallableStatement callableStatement = connection
					.prepareCall("{CALL EZEE_SP_BOOKING_IUD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			callableStatement.setString(++pindex, booking.getCode());
			callableStatement.setInt(++pindex, booking.getUserDTO().getId());
			callableStatement.setInt(++pindex, booking.getTripDTO().getId());
			callableStatement.setString(++pindex, booking.getTravelDate());
			callableStatement.setInt(++pindex, booking.getTravelerCount());
			callableStatement.setBigDecimal(++pindex, booking.getPricePerPerson());
			callableStatement.setBigDecimal(++pindex, booking.getTotalPrice());
			callableStatement.setInt(++pindex, booking.getActiveFlag());
			callableStatement.setString(++pindex, booking.getUpdatedBy());
			callableStatement.registerOutParameter(++pindex, Types.INTEGER);

			callableStatement.execute();
			affected = callableStatement.getInt(pindex);

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return affected;
	}

	public BookingDTO getBooking(BookingDTO booking) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			@Cleanup
			PreparedStatement statement = null;

			if (booking.getId() != 0) {
				String query = "SELECT id, code, customer_id, trip_id, travel_date, traveler_count, price_per_person, total_price, active_flag, updated_by, updated_at FROM booking WHERE id = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, booking.getId());
			} else {
				String query = "SELECT id, code, customer_id, trip_id, travel_date, traveler_count, price_per_person, total_price, active_flag, updated_by, updated_at FROM booking WHERE code = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, booking.getCode());
			}

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				booking.setId(resultSet.getInt("id"));
				booking.setCode(resultSet.getString("code"));
				UserDTO userDTO = new UserDTO();
				userDTO.setId(resultSet.getInt("customer_id"));
				booking.setUserDTO(userDTO);
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				booking.setTripDTO(tripDTO);
				booking.setTravelDate(resultSet.getString("travel_date"));
				booking.setTravelerCount(resultSet.getInt("traveler_count"));
				booking.setPricePerPerson(resultSet.getBigDecimal("price_per_person"));
				booking.setTotalPrice(resultSet.getBigDecimal("total_price"));
				booking.setActiveFlag(resultSet.getInt("active_flag"));
				booking.setUpdatedBy(resultSet.getString("updated_by"));
				booking.setUpdatedAt(resultSet.getString("updated_at"));
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return booking;
	}

	public List<BookingDTO> getAllBookings() {
		List<BookingDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT id, code, customer_id, trip_id, travel_date, traveler_count, price_per_person, total_price, active_flag, updated_by, updated_at FROM booking WHERE active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				BookingDTO dto = new BookingDTO();
				dto.setId(resultSet.getInt("id"));
				dto.setCode(resultSet.getString("code"));
				UserDTO userDTO = new UserDTO();
				userDTO.setId(resultSet.getInt("customer_id"));
				dto.setUserDTO(userDTO);
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				dto.setTripDTO(tripDTO);
				dto.setTravelDate(resultSet.getString("travel_date"));
				dto.setTravelerCount(resultSet.getInt("traveler_count"));
				dto.setPricePerPerson(resultSet.getBigDecimal("price_per_person"));
				dto.setTotalPrice(resultSet.getBigDecimal("total_price"));
				dto.setActiveFlag(resultSet.getInt("active_flag"));
				dto.setUpdatedBy(resultSet.getString("updated_by"));
				dto.setUpdatedAt(resultSet.getString("updated_at"));
				list.add(dto);
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}
}
