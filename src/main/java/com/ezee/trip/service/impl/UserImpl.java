package com.ezee.trip.service.impl;


import java.util.List;

import com.ezee.trip.dao.UserDAO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.dto.UserDTO;
import com.ezee.trip.exception.ErrorCode;
import com.ezee.trip.exception.ServiceException;
import com.ezee.trip.service.AuthService;
import com.ezee.trip.service.UserService;
import com.ezee.trip.util.CodeGenarator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDAO userDAO;

    private static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.impl");

    public void addUser(UserDTO userDTO, String authCode) {
        try {
            AuthResponseDTO authResponse = authService.validateAuthCode(authCode);
            userDTO.setUpdatedBy(authResponse.getUsername());
            userDTO.setCode(CodeGenarator.generateCode("USR", 12));
            userDAO.addUser(userDTO);
        } catch (Exception e) {
            LOGGER.error("Error while adding user: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while inserting user");
        }
    }

    public UserDTO getUserByCode(String code, String authCode) {
        UserDTO userDTO = new UserDTO();
        try {
            authService.validateAuthCode(authCode);
            userDTO.setCode(code);
            userDTO = userDAO.getUser(userDTO);

            if (userDTO.getId() == 0) {
                throw new ServiceException(ErrorCode.ID_OR_CODE_NOT_FOUND_EXCEPTION, "User not found");
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error while fetching user by code: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching user");
        }
        return userDTO;
    }

    public List<UserDTO> getAllUser(String authCode) {
        try {
            authService.validateAuthCode(authCode);
            return userDAO.getAllUserDTO();
        } catch (Exception e) {
            LOGGER.error("Error while fetching all users: {}", e.getMessage(), e);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error while fetching users");
        }
    }
}

