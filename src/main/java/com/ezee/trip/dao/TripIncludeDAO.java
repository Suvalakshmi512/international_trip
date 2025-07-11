package com.ezee.trip.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.TripIncludeDTO;
import com.ezee.trip.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class TripIncludeDAO {
	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.dao");

	public int addTripInclude(TripIncludeDTO tripIncludeDTO) {
		int affected = 0;
		try {
			int pindex = 0;
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_TRIP_INCLUDE_IUD(?, ?, ?, ?, ?, ?)}");

			callableStatement.setString(++pindex, tripIncludeDTO.getCode());
			callableStatement.setString(++pindex, tripIncludeDTO.getTripDTO().getCode());
			callableStatement.setString(++pindex, tripIncludeDTO.getItem());
			callableStatement.setInt(++pindex, tripIncludeDTO.getActiveFlag());
			callableStatement.setString(++pindex, tripIncludeDTO.getUpdatedBy());
			callableStatement.registerOutParameter(++pindex, Types.INTEGER);
			callableStatement.execute();
			affected = callableStatement.getInt(pindex);

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return affected;
	}

	public TripIncludeDTO getTripInclude(TripIncludeDTO tripInclude) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			@Cleanup
			PreparedStatement statement = null;

			if (tripInclude.getId() != 0) {
				String query = "SELECT id, code, trip_id, item, active_flag, updated_by, updated_at FROM trip_include WHERE id = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, tripInclude.getId());
			} else {
				String query = "SELECT id, code, trip_id, item, active_flag, updated_by, updated_at FROM trip_include WHERE code = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, tripInclude.getCode());
			}

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				tripInclude.setId(resultSet.getInt("id"));
				tripInclude.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				tripInclude.setTripDTO(tripDTO);
				tripInclude.setItem(resultSet.getString("item"));
				tripInclude.setActiveFlag(resultSet.getInt("active_flag"));
				tripInclude.setUpdatedBy(resultSet.getString("updated_by"));
				tripInclude.setUpdatedAt(resultSet.getString("updated_at"));
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return tripInclude;
	}

	public List<TripIncludeDTO> getAllTripIncludeDTO() {
		List<TripIncludeDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT id, code, trip_id, item, active_flag, updated_by, updated_at FROM trip_include WHERE active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				TripIncludeDTO tripInclude = new TripIncludeDTO();
				tripInclude.setId(resultSet.getInt("id"));
				tripInclude.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				tripInclude.setTripDTO(tripDTO);
				tripInclude.setItem(resultSet.getString("item"));
				tripInclude.setActiveFlag(resultSet.getInt("active_flag"));
				tripInclude.setUpdatedBy(resultSet.getString("updated_by"));
				tripInclude.setUpdatedAt(resultSet.getString("updated_at"));
				list.add(tripInclude);
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}
	public List<TripIncludeDTO> getAllTripIncludeByTrip(TripDTO trip) {
		List<TripIncludeDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			int id = trip.getId();

			String query = "SELECT id, code, trip_id, item, active_flag, updated_by, updated_at FROM trip_include WHERE trip_id = ? and active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				TripIncludeDTO tripInclude = new TripIncludeDTO();
				tripInclude.setId(resultSet.getInt("id"));
				tripInclude.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				tripInclude.setTripDTO(tripDTO);
				tripInclude.setItem(resultSet.getString("item"));
				tripInclude.setActiveFlag(resultSet.getInt("active_flag"));
				tripInclude.setUpdatedBy(resultSet.getString("updated_by"));
				tripInclude.setUpdatedAt(resultSet.getString("updated_at"));
				list.add(tripInclude);
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
		return list;
	}
}
