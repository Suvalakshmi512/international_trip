package com.ezee.trip.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class TripDAO {

	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.dao");

	public int addTrip(TripDTO tripDTO) {
		int affected = 0;
		try {
			int pindex = 0;
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			CallableStatement callableStatement = connection
					.prepareCall("{CALL EZEE_SP_TRIP_IUD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			callableStatement.setString(++pindex, tripDTO.getCode());
			callableStatement.setString(++pindex, tripDTO.getDestinationDTO().getCode());
			callableStatement.setString(++pindex, tripDTO.getName());
			callableStatement.setInt(++pindex, tripDTO.getTotalSeat());
			callableStatement.setInt(++pindex, tripDTO.getAvailableSeat());
			callableStatement.setString(++pindex, tripDTO.getDurationDays());
			callableStatement.setBigDecimal(++pindex, tripDTO.getPrice());
			callableStatement.setString(++pindex, tripDTO.getCurrency());
			callableStatement.setInt(++pindex, tripDTO.getActiveFlag());
			callableStatement.setString(++pindex, tripDTO.getUpdatedBy());
			callableStatement.registerOutParameter(++pindex, Types.INTEGER);

			callableStatement.execute();
			affected = callableStatement.getInt(pindex);

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return affected;
	}

	public TripDTO getTrip(TripDTO tripDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			@Cleanup
			PreparedStatement statement = null;

			if (tripDTO.getId() != 0) {
				String query = "SELECT id, code, trip_name, destination_id, total_seat, available_seat, duration_days, price, currency, active_flag, updated_by, updated_at FROM trip WHERE id = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, tripDTO.getId());
			} else {
				String query = "SELECT id, code, trip_name, destination_id, total_seat, available_seat, duration_days, price, currency, active_flag, updated_by, updated_at FROM trip WHERE code = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, tripDTO.getCode());
			}

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				tripDTO.setId(resultSet.getInt("id"));
				tripDTO.setCode(resultSet.getString("code"));
				tripDTO.setName(resultSet.getString("trip_name"));
				DestinationDTO destinationDTO = new DestinationDTO();
				destinationDTO.setId(resultSet.getInt("destination_id"));
				tripDTO.setDestinationDTO(destinationDTO);
				tripDTO.setTotalSeat(resultSet.getInt("total_seat"));
				tripDTO.setAvailableSeat(resultSet.getInt("available_seat"));
				tripDTO.setDurationDays(resultSet.getString("duration_days"));
				tripDTO.setCurrency(resultSet.getString("currency"));
				tripDTO.setActiveFlag(resultSet.getInt("active_flag"));
				tripDTO.setUpdatedBy(resultSet.getString("updated_by"));
				tripDTO.setUpdatedAt(resultSet.getString("updated_at"));
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return tripDTO;
	}

	public List<TripDTO> getAllTripDTO() {
		List<TripDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT id, code, trip_name, destination_id, total_seat, available_seat, duration_days, price, currency, active_flag, updated_by, updated_at FROM trip WHERE active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("id"));
				tripDTO.setCode(resultSet.getString("code"));
				tripDTO.setName(resultSet.getString("trip_name"));
				DestinationDTO destinationDTO = new DestinationDTO();
				destinationDTO.setId(resultSet.getInt("destination_id"));
				tripDTO.setDestinationDTO(destinationDTO);
				tripDTO.setTotalSeat(resultSet.getInt("total_seat"));
				tripDTO.setAvailableSeat(resultSet.getInt("available_seat"));
				tripDTO.setDurationDays(resultSet.getString("duration_days"));
				tripDTO.setPrice(resultSet.getBigDecimal("price"));
				tripDTO.setCurrency(resultSet.getString("currency"));
				tripDTO.setActiveFlag(resultSet.getInt("active_flag"));
				tripDTO.setUpdatedBy(resultSet.getString("updated_by"));
				tripDTO.setUpdatedAt(resultSet.getString("updated_at"));
				list.add(tripDTO);
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}
}
