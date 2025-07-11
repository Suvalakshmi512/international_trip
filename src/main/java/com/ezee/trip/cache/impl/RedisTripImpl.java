package com.ezee.trip.cache.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.trip.cache.RedisTripService;
import com.ezee.trip.cache.dto.TripCacheDTO;
import com.ezee.trip.dao.TripDAO;
import com.ezee.trip.dto.TripDTO;

@Service
public class RedisTripImpl implements RedisTripService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private TripDAO tripDAO;

    private static final String TRIP_CACHE_NAME = "TRIP_CACHE";

    public TripDTO getTripFromCache(TripDTO tripDTO) {
        TripDTO response = null;
        String cacheKey = "TRIP_" + tripDTO.getId();  
        ValueWrapper wrapper = cacheManager.getCache(TRIP_CACHE_NAME).get(cacheKey);
        if (wrapper != null && wrapper.get() != null) {
            TripCacheDTO cacheDTO = (TripCacheDTO) wrapper.get();
            response = convertToTripDTO(cacheDTO, tripDTO.getId());
        }

        if (response == null) {
            response = tripDAO.getTrip(tripDTO);
        }

        if (response != null && response.getId() != 0) {
            TripCacheDTO cacheDTO = convertToCacheDTO(response);
            putTripCache(cacheKey, cacheDTO);
        }

        return response;
    }

    public void putTripCache(String cacheKey, TripCacheDTO cacheDTO) {
        Cache cache = cacheManager.getCache(TRIP_CACHE_NAME);
        if (cache != null) {
            cache.put(cacheKey, cacheDTO);
        }
    }

    private TripCacheDTO convertToCacheDTO(TripDTO dto) {
        TripCacheDTO cacheDTO = new TripCacheDTO();
        cacheDTO.setId(dto.getId());
        cacheDTO.setCode(dto.getCode());
        cacheDTO.setName(dto.getName());
        cacheDTO.setActiveFlag(dto.getActiveFlag());
        cacheDTO.setDestinationDTO(dto.getDestinationDTO());
        cacheDTO.setTripHighlight(dto.getTripHighlight());
        cacheDTO.setTripInclude(dto.getTripInclude());
        cacheDTO.setAvailableDate(dto.getAvailableDate());
        cacheDTO.setTotalSeat(dto.getTotalSeat());
        cacheDTO.setAvailableSeat(dto.getAvailableSeat());
        cacheDTO.setDurationDays(dto.getDurationDays());
        cacheDTO.setPrice(dto.getPrice());
        cacheDTO.setCurrency(dto.getCurrency());
        cacheDTO.setUpdatedBy(dto.getUpdatedBy());
        cacheDTO.setUpdatedAt(dto.getUpdatedAt());
        return cacheDTO;
    }

    private TripDTO convertToTripDTO(TripCacheDTO cacheDTO, Integer tripId) {
        TripDTO dto = new TripDTO();
        dto.setId(tripId);
        dto.setCode(cacheDTO.getCode());
        dto.setName(cacheDTO.getName());
        dto.setActiveFlag(cacheDTO.getActiveFlag());
        dto.setDestinationDTO(cacheDTO.getDestinationDTO());
        dto.setTripHighlight(cacheDTO.getTripHighlight());
        dto.setTripInclude(cacheDTO.getTripInclude());
        dto.setAvailableDate(cacheDTO.getAvailableDate());
        dto.setTotalSeat(cacheDTO.getTotalSeat());
        dto.setAvailableSeat(cacheDTO.getAvailableSeat());
        dto.setDurationDays(cacheDTO.getDurationDays());
        dto.setPrice(cacheDTO.getPrice());
        dto.setCurrency(cacheDTO.getCurrency());
        dto.setUpdatedBy(cacheDTO.getUpdatedBy());
        dto.setUpdatedAt(cacheDTO.getUpdatedAt());
        return dto;
    }
}

