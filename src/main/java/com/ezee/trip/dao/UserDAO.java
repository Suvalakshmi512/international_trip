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

import com.ezee.trip.dto.UserDTO;
import com.ezee.trip.exception.ServiceException;

import lombok.Cleanup;

@Repository
public class UserDAO {
	@Autowired
	private DataSource dataSource;

	public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.dao");

	public int addUser(UserDTO userDTO) {
		int affected = 0;
		try {
			int pindex = 0;
			@Cleanup
			Connection connection = dataSource.getConnection();

			@Cleanup
			CallableStatement callableStatement = connection
					.prepareCall("{CALL EZEE_SP_USER_IUD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			callableStatement.setString(++pindex, userDTO.getCode());
			callableStatement.setString(++pindex, userDTO.getName());
			callableStatement.setString(++pindex, userDTO.getPhoneNo());
			callableStatement.setString(++pindex, userDTO.getEmail());
			callableStatement.setString(++pindex, userDTO.getGender());
			callableStatement.setString(++pindex, userDTO.getDateOfBirth());
			callableStatement.setString(++pindex, userDTO.getAddress());
			callableStatement.setInt(++pindex, userDTO.getActiveFlag());
			callableStatement.setString(++pindex, userDTO.getUpdatedBy());
			callableStatement.registerOutParameter(++pindex, Types.INTEGER);
			callableStatement.execute();
			affected = callableStatement.getInt(pindex);

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return affected;
	}

	public UserDTO getUser(UserDTO userDTO) {
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();
			@Cleanup
			PreparedStatement statement = null;

			if (userDTO.getId() != 0) {
				String query = "SELECT id, code, name, phone_no, email, gender, date_of_birth, address, active_flag, updated_by, updated_at FROM user WHERE id = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setInt(1, userDTO.getId());
			} else {
				String query = "SELECT id, code, name, phone_no, email, gender, date_of_birth, address, active_flag, updated_by, updated_at FROM user WHERE code = ? AND active_flag = 1";
				statement = connection.prepareStatement(query);
				statement.setString(1, userDTO.getCode());
			}

			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				userDTO.setId(resultSet.getInt("id"));
				userDTO.setCode(resultSet.getString("code"));
				userDTO.setName(resultSet.getString("name"));
				userDTO.setPhoneNo(resultSet.getString("phone_no"));
				userDTO.setEmail(resultSet.getString("email"));
				userDTO.setGender(resultSet.getString("gender"));
				userDTO.setDateOfBirth(resultSet.getString("date_of_birth"));
				userDTO.setAddress(resultSet.getString("address"));
				userDTO.setActiveFlag(resultSet.getInt("active_flag"));
				userDTO.setUpdatedBy(resultSet.getString("updated_by"));
				userDTO.setUpdatedAt(resultSet.getString("updated_at"));
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return userDTO;
	}

	public List<UserDTO> getAllUserDTO() {
		List<UserDTO> list = new ArrayList<>();
		try {
			@Cleanup
			Connection connection = dataSource.getConnection();

			String query = "SELECT id, code, name, phone_no, email, gender, date_of_birth, address, active_flag, updated_by, updated_at FROM user WHERE active_flag = 1";

			@Cleanup
			PreparedStatement statement = connection.prepareStatement(query);
			@Cleanup
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				UserDTO userDTO = new UserDTO();
				userDTO.setId(resultSet.getInt("id"));
				userDTO.setCode(resultSet.getString("code"));
				userDTO.setName(resultSet.getString("name"));
				userDTO.setPhoneNo(resultSet.getString("phone_no"));
				userDTO.setEmail(resultSet.getString("email"));
				userDTO.setGender(resultSet.getString("gender"));
				userDTO.setDateOfBirth(resultSet.getString("date_of_birth"));
				userDTO.setAddress(resultSet.getString("address"));
				userDTO.setActiveFlag(resultSet.getInt("active_flag"));
				userDTO.setUpdatedBy(resultSet.getString("updated_by"));
				userDTO.setUpdatedAt(resultSet.getString("updated_at"));
				list.add(userDTO);
			}

		} catch (Exception e) {
			LOGGER.error("\nMessage: {}, \nerror: {}", e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		return list;
	}

}
