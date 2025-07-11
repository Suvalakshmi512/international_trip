package com.ezee.trip.cache.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ezee.trip.cache.RedisTripIncludeService;
import com.ezee.trip.cache.dto.TripIncludeCacheDTO;
import com.ezee.trip.dao.TripIncludeDAO;
import com.ezee.trip.dto.TripIncludeDTO;

@Service
public class RedisTripIncludeImpl implements RedisTripIncludeService {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private TripIncludeDAO tripIncludeDAO;

	private static final String TRIP_INCLUDE_CACHE_NAME = "TRIP_INCLUDE_CACHE";

	@Override
	public TripIncludeDTO getTripIncludeFromCache(int id) {
		TripIncludeDTO response = null;
		String cacheKey = "TRIP_INCLUDE_" + id;

		ValueWrapper wrapper = cacheManager.getCache(TRIP_INCLUDE_CACHE_NAME).get(cacheKey);
		if (wrapper != null && wrapper.get() != null) {
			TripIncludeCacheDTO cacheDTO = (TripIncludeCacheDTO) wrapper.get();
			response = convertToTripIncludeDTO(cacheDTO);
		}

		if (response == null) {
//			response = tripIncludeDAO.getAllTripIncludeByTrip(id);
		}

		if (response != null && response.getId() != 0) {
			TripIncludeCacheDTO cacheDTO = convertToCacheDTO(response);
			putTripIncludeCache(cacheKey, cacheDTO);
		}

		return response;
	}

	@Override
	public void putTripIncludeCache(String cacheKey, TripIncludeCacheDTO cacheDTO) {
		Cache cache = cacheManager.getCache(TRIP_INCLUDE_CACHE_NAME);
		if (cache != null) {
			cache.put(cacheKey, cacheDTO);
		}
	}

	private TripIncludeCacheDTO convertToCacheDTO(TripIncludeDTO dto) {
		TripIncludeCacheDTO cacheDTO = new TripIncludeCacheDTO();
		cacheDTO.setId(dto.getId());
		cacheDTO.setCode(dto.getCode());
		cacheDTO.setTripDTO(dto.getTripDTO());
		cacheDTO.setItem(dto.getItem());
		cacheDTO.setActiveFlag(dto.getActiveFlag());
		cacheDTO.setUpdatedBy(dto.getUpdatedBy());
		cacheDTO.setUpdatedAt(dto.getUpdatedAt());
		return cacheDTO;
	}

	private TripIncludeDTO convertToTripIncludeDTO(TripIncludeCacheDTO cacheDTO) {
		TripIncludeDTO dto = new TripIncludeDTO();
		dto.setId(cacheDTO.getId());
		dto.setCode(cacheDTO.getCode());
		dto.setTripDTO(cacheDTO.getTripDTO());
		dto.setItem(cacheDTO.getItem());
		dto.setActiveFlag(cacheDTO.getActiveFlag());
		dto.setUpdatedBy(cacheDTO.getUpdatedBy());
		dto.setUpdatedAt(cacheDTO.getUpdatedAt());
		return dto;
	}
}
