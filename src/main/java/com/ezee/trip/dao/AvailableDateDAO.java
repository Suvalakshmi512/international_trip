package com.ezee.trip.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ezee.trip.dto.AvailableDateDTO;
import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.exception.ServiceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Cleanup;

@Repository
public class AvailableDateDAO {

	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.dao");

	public int addAvailableDate(AvailableDateDTO availableDate) {
		int affected = 0;
		try {
			int pindex = 0;
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_AVAILABLE_DATE_IUD(?, ?, ?, ?, ?, ?)}");

			callableStatement.setString(++pindex, availableDate.getCode());
			callableStatement.setString(++pindex, availableDate.getTripDTO().getCode());
			callableStatement.setString(++pindex, availableDate.getAvailableDate());
			callableStatement.setInt(++pindex, availableDate.getActiveFlag());
			callableStatement.setString(++pindex, availableDate.getUpdatedBy());
			callableStatement.registerOutParameter(++pindex, Types.INTEGER);

			callableStatement.execute();
			affected = callableStatement.getInt(pindex);

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return affected;
	}

	public AvailableDateDTO getAvailableDate(AvailableDateDTO availableDate) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			@Cleanup
			PreparedStatement statement = null;

			if (availableDate.getId() != 0) {
				String query = "SELECT id, code, trip_id, available_date, active_flag, updated_by, updated_at FROM available_date WHERE id = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, availableDate.getId());
			} else {
				String query = "SELECT id, code, trip_id, available_date, active_flag, updated_by, updated_at FROM available_date WHERE code = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, availableDate.getCode());
			}

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				availableDate.setId(resultSet.getInt("id"));
				availableDate.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				availableDate.setTripDTO(tripDTO);
				availableDate.setAvailableDate(resultSet.getString("available_date"));
				availableDate.setActiveFlag(resultSet.getInt("active_flag"));
				availableDate.setUpdatedBy(resultSet.getString("updated_by"));
				availableDate.setUpdatedAt(resultSet.getString("updated_at"));
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return availableDate;
	}

	public List<AvailableDateDTO> getAllAvailableDate() {
		List<AvailableDateDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT id, code, trip_id, available_date, active_flag, updated_by, updated_at FROM available_date WHERE active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				AvailableDateDTO dto = new AvailableDateDTO();
				dto.setId(resultSet.getInt("id"));
				dto.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				dto.setTripDTO(tripDTO);
				dto.setAvailableDate(resultSet.getString("available_date"));
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
	public List<AvailableDateDTO> getAllAvailableDateByTrip(TripDTO trip) {
		List<AvailableDateDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			int id = trip.getId();

			String query = "SELECT id, code, trip_id, available_date, active_flag, updated_by, updated_at FROM available_date WHERE trip_id = ? and active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				AvailableDateDTO dto = new AvailableDateDTO();
				dto.setId(resultSet.getInt("id"));
				dto.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				dto.setTripDTO(tripDTO);
				dto.setAvailableDate(resultSet.getString("available_date"));
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
