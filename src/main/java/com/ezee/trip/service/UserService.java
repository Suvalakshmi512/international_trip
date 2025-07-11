package com.ezee.trip.service;

import java.util.List;

import com.ezee.trip.dto.UserDTO;

public interface UserService {
    public void addUser(UserDTO userDTO, String authCode);
    public UserDTO getUserByCode(String code, String authCode);
    public List<UserDTO> getAllUser(String authCode);


}
