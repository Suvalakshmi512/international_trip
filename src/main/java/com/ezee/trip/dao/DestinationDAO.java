package com.ezee.trip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ezee.trip.dto.DestinationDTO;
import com.ezee.trip.exception.ServiceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Cleanup;

@Repository
public class DestinationDAO {
	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.dao");

	public int addDestination(DestinationDTO destinationDTO) {
		int affected = 0;
		try {
			int pindex = 0;
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			CallableStatement callableStatement = connection
					.prepareCall("{CALL EZEE_SP_DESTINATION_IUD(?, ?, ?, ?, ?, ?)}");
			callableStatement.setString(++pindex, destinationDTO.getCode());
			callableStatement.setString(++pindex, destinationDTO.getCity());
			callableStatement.setString(++pindex, destinationDTO.getCountry());
			callableStatement.setInt(++pindex, destinationDTO.getActiveFlag());
			callableStatement.setString(++pindex, destinationDTO.getUpdatedBy());
			callableStatement.registerOutParameter(++pindex, Types.INTEGER);
			callableStatement.execute();
			affected = callableStatement.getInt(pindex);

		} catch (Exception e) {
			LOGGER.error("\nMessage: {},\nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return affected;
	}

	public DestinationDTO getDestination(DestinationDTO dto) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			PreparedStatement statement = null;

			if (dto.getId() != 0) {
				String query = "SELECT id, code, city, country, active_flag, updated_by, updated_at FROM destination WHERE id = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, dto.getId());
			} else {
				String query = "SELECT id, code, city, country, active_flag, updated_by, updated_at FROM destination WHERE code = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, dto.getCode());
			}

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				dto.setId(resultSet.getInt("id"));
				dto.setCode(resultSet.getString("code"));
				dto.setCity(resultSet.getString("city"));
				dto.setCountry(resultSet.getString("country"));
				dto.setActiveFlag(resultSet.getInt("active_flag"));
				dto.setUpdatedBy(resultSet.getString("updated_by"));
				dto.setUpdatedAt(resultSet.getString("updated_at"));
			}
		} catch (Exception e) {
			LOGGER.error("\nMessage: {},\nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return dto;
	}

	public List<DestinationDTO> getAllDestinations() {
		List<DestinationDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT id, code, city, country, active_flag, updated_by, updated_at FROM destination WHERE active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				DestinationDTO dto = new DestinationDTO();
				dto.setId(resultSet.getInt("id"));
				dto.setCode(resultSet.getString("code"));
				dto.setCity(resultSet.getString("city"));
				dto.setCountry(resultSet.getString("country"));
				dto.setActiveFlag(resultSet.getInt("active_flag"));
				dto.setUpdatedBy(resultSet.getString("updated_by"));
				dto.setUpdatedAt(resultSet.getString("updated_at"));
				list.add(dto);
			}
		} catch (Exception e) {
			LOGGER.error("\nMessage: {},\nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return list;
	}
}
