package com.ezee.trip.cache.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.trip.cache.RedisAvailableDateService;
import com.ezee.trip.cache.dto.AvailableDateCacheDTO;
import com.ezee.trip.dao.AvailableDateDAO;
import com.ezee.trip.dto.AvailableDateDTO;

@Service
public class RedisAvailableDateImpl implements RedisAvailableDateService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private AvailableDateDAO availableDateDAO;

    private static final String AVAILABLE_DATE_CACHE_NAME = "AVAILABLE_DATE_CACHE";

    @Override
    public AvailableDateDTO getAvailableDateFromCache(int id) {
        AvailableDateDTO response = null;
        String cacheKey = "AVAILABLE_DATE_" + id;  

        ValueWrapper wrapper = cacheManager.getCache(AVAILABLE_DATE_CACHE_NAME).get(cacheKey);
        if (wrapper != null && wrapper.get() != null) {
            AvailableDateCacheDTO cacheDTO = (AvailableDateCacheDTO) wrapper.get();
            response = convertToAvailableDateDTO(cacheDTO);
        }

        if (response == null) {
//            response = availableDateDAO.getAllAvailableDateByTrip(id);
        }

        if (response != null && response.getId() != 0) {
            AvailableDateCacheDTO cacheDTO = convertToCacheDTO(response);
            putAvailableDateCache(cacheKey, cacheDTO);
        }

        return response;
    }

    @Override
    public void putAvailableDateCache(String cacheKey, AvailableDateCacheDTO cacheDTO) {
        Cache cache = cacheManager.getCache(AVAILABLE_DATE_CACHE_NAME);
        if (cache != null) {
            cache.put(cacheKey, cacheDTO);
        }
    }

    private AvailableDateCacheDTO convertToCacheDTO(AvailableDateDTO dto) {
        AvailableDateCacheDTO cacheDTO = new AvailableDateCacheDTO();
        cacheDTO.setId(dto.getId());
        cacheDTO.setCode(dto.getCode());
        cacheDTO.setTripDTO(dto.getTripDTO());
        cacheDTO.setAvailableDate(dto.getAvailableDate());
        cacheDTO.setActiveFlag(dto.getActiveFlag());
        cacheDTO.setUpdatedBy(dto.getUpdatedBy());
        cacheDTO.setUpdatedAt(dto.getUpdatedAt());
        return cacheDTO;
    }

    private AvailableDateDTO convertToAvailableDateDTO(AvailableDateCacheDTO cacheDTO) {
        AvailableDateDTO dto = new AvailableDateDTO();
        dto.setId(cacheDTO.getId());
        dto.setCode(cacheDTO.getCode());
        dto.setTripDTO(cacheDTO.getTripDTO());
        dto.setAvailableDate(cacheDTO.getAvailableDate());
        dto.setActiveFlag(cacheDTO.getActiveFlag());
        dto.setUpdatedBy(cacheDTO.getUpdatedBy());
        dto.setUpdatedAt(cacheDTO.getUpdatedAt());
        return dto;
    }
}

