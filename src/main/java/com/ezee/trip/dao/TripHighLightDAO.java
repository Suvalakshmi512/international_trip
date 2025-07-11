package com.ezee.trip.dao;

import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ezee.trip.dto.TripDTO;
import com.ezee.trip.dto.TripHighLightDTO;
import com.ezee.trip.exception.ServiceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Cleanup;

@Repository
public class TripHighLightDAO {

	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.dao");

	public int addTripHighLight(TripHighLightDTO tripHighlight) {
		int affected = 0;
		try {
			int pindex = 0;
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			CallableStatement callableStatement = connection.prepareCall("{CALL EZEE_SP_TRIP_HIGHLIGHT_IUD( ?, ?, ?, ?, ?, ?)}");

			callableStatement.setString(++pindex, tripHighlight.getCode());
			callableStatement.setString(++pindex, tripHighlight.getTripDTO().getCode());
			callableStatement.setString(++pindex, tripHighlight.getHighLight());
			callableStatement.setInt(++pindex, tripHighlight.getActiveFlag());
			callableStatement.setString(++pindex, tripHighlight.getUpdatedBy());
			callableStatement.registerOutParameter(++pindex, Types.INTEGER);

			callableStatement.execute();
			affected = callableStatement.getInt(pindex);

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return affected;
	}

	public TripHighLightDTO getTripHighLight(TripHighLightDTO tripHighlight) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			@Cleanup
			PreparedStatement statement = null;

			if (tripHighlight.getId() != 0) {
				String query = "SELECT id, code, trip_id, highlight, active_flag, updated_by, updated_at FROM trip_highlight WHERE id = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, tripHighlight.getId());
			} else {
				String query = "SELECT id, code, trip_id, highlight, active_flag, updated_by, updated_at FROM trip_highlight WHERE code = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, tripHighlight.getCode());
			}

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				tripHighlight.setId(resultSet.getInt("id"));
				tripHighlight.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				tripHighlight.setTripDTO(tripDTO);
				tripHighlight.setHighLight(resultSet.getString("highlight"));
				tripHighlight.setActiveFlag(resultSet.getInt("active_flag"));
				tripHighlight.setUpdatedBy(resultSet.getString("updated_by"));
				tripHighlight.setUpdatedAt(resultSet.getString("updated_at"));
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return tripHighlight;
	}

	public List<TripHighLightDTO> getAllTripHighLight() {
		List<TripHighLightDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT id, code, trip_id, highlight, active_flag, updated_by, updated_at FROM trip_highlight WHERE active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				TripHighLightDTO tripHighlight = new TripHighLightDTO();
				tripHighlight.setId(resultSet.getInt("id"));
				tripHighlight.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				tripHighlight.setTripDTO(tripDTO);
				tripHighlight.setHighLight(resultSet.getString("highlight"));
				tripHighlight.setActiveFlag(resultSet.getInt("active_flag"));
				tripHighlight.setUpdatedBy(resultSet.getString("updated_by"));
				tripHighlight.setUpdatedAt(resultSet.getString("updated_at"));
				list.add(tripHighlight);
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}
	public List<TripHighLightDTO> getAllTripHighLightByTrip(TripDTO trip) {
		List<TripHighLightDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT id, code, trip_id, highlight, active_flag, updated_by, updated_at FROM trip_highlight WHERE trip_id = ? and active_flag = 1";
			int id = trip.getId();
			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				TripHighLightDTO tripHighlight = new TripHighLightDTO();
				tripHighlight.setId(resultSet.getInt("id"));
				tripHighlight.setCode(resultSet.getString("code"));
				TripDTO tripDTO = new TripDTO();
				tripDTO.setId(resultSet.getInt("trip_id"));
				tripHighlight.setTripDTO(tripDTO);
				tripHighlight.setHighLight(resultSet.getString("highlight"));
				tripHighlight.setActiveFlag(resultSet.getInt("active_flag"));
				tripHighlight.setUpdatedBy(resultSet.getString("updated_by"));
				tripHighlight.setUpdatedAt(resultSet.getString("updated_at"));
				list.add(tripHighlight);
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}
}
