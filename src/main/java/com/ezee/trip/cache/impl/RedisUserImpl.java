package com.ezee.trip.cache.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.trip.cache.RedisUserService;
import com.ezee.trip.cache.dto.UserCacheDTO;
import com.ezee.trip.dao.UserDAO;
import com.ezee.trip.dto.UserDTO;

@Service
public class RedisUserImpl implements RedisUserService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserDAO userDAO;

    private static final String USER_CACHE_NAME = "USER_CACHE";

    @Override
    public UserDTO getUserFromCache(UserDTO userDTO) {
        UserDTO response = null;
        String cacheKey = "USER_" + userDTO.getId(); 

        ValueWrapper wrapper = cacheManager.getCache(USER_CACHE_NAME).get(cacheKey);
        if (wrapper != null && wrapper.get() != null) {
            UserCacheDTO cacheDTO = (UserCacheDTO) wrapper.get();
            response = convertToUserDTO(cacheDTO);
        }

        if (response == null) {
            response = userDAO.getUser(userDTO);
        }

        if (response != null && response.getId() != 0) {
            UserCacheDTO cacheDTO = convertToCacheDTO(response);
            putUserCache(cacheKey, cacheDTO);
        }

        return response;
    }

    @Override
    public void putUserCache(String cacheKey, UserCacheDTO cacheDTO) {
        Cache cache = cacheManager.getCache(USER_CACHE_NAME);
        if (cache != null) {
            cache.put(cacheKey, cacheDTO);
        }
    }

    private UserCacheDTO convertToCacheDTO(UserDTO dto) {
        UserCacheDTO cacheDTO = new UserCacheDTO();
        cacheDTO.setId(dto.getId());
        cacheDTO.setName(dto.getName());
        cacheDTO.setCode(dto.getCode());
        cacheDTO.setPhoneNo(dto.getPhoneNo());
        cacheDTO.setEmail(dto.getEmail());
        cacheDTO.setGender(dto.getGender());
        cacheDTO.setDateOfBirth(dto.getDateOfBirth());
        cacheDTO.setAddress(dto.getAddress());
        cacheDTO.setActiveFlag(dto.getActiveFlag());
        cacheDTO.setUpdatedBy(dto.getUpdatedBy());
        cacheDTO.setUpdatedAt(dto.getUpdatedAt());
        return cacheDTO;
    }

    private UserDTO convertToUserDTO(UserCacheDTO cacheDTO) {
        UserDTO dto = new UserDTO();
        dto.setId(cacheDTO.getId());
        dto.setName(cacheDTO.getName());
        dto.setCode(cacheDTO.getCode());
        dto.setPhoneNo(cacheDTO.getPhoneNo());
        dto.setEmail(cacheDTO.getEmail());
        dto.setGender(cacheDTO.getGender());
        dto.setDateOfBirth(cacheDTO.getDateOfBirth());
        dto.setAddress(cacheDTO.getAddress());
        dto.setActiveFlag(cacheDTO.getActiveFlag());
        dto.setUpdatedBy(cacheDTO.getUpdatedBy());
        dto.setUpdatedAt(cacheDTO.getUpdatedAt());
        return dto;
    }
}
