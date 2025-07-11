//package com.ezee.trip.cache.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.Cache;
//import org.springframework.cache.Cache.ValueWrapper;
//import org.springframework.cache.CacheManager;
//import org.springframework.stereotype.Service;
//
//import com.ezee.trip.cache.RedisTripHighLightService;
//import com.ezee.trip.cache.dto.TripHighLightCacheDTO;
//import com.ezee.trip.dao.TripHighLightDAO;
//import com.ezee.trip.dto.TripHighLightDTO;
//
//@Service
//public class RedisTripHighLightImpl implements RedisTripHighLightService {
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @Autowired
//    private TripHighLightDAO tripHighLightDAO;
//
//    private static final String TRIP_HIGHLIGHT_CACHE_NAME = "TRIP_HIGHLIGHT_CACHE";
//
//    @Override
//    public TripHighLightDTO getTripHighLightFromCache(int id) {
//        TripHighLightDTO response = null;
//        String cacheKey = "TRIP_HIGHLIGHT_" + id; 
//
//        ValueWrapper wrapper = cacheManager.getCache(TRIP_HIGHLIGHT_CACHE_NAME).get(cacheKey);
//        if (wrapper != null && wrapper.get() != null) {
//            TripHighLightCacheDTO cacheDTO = (TripHighLightCacheDTO) wrapper.get();
//            response = convertToTripHighLightDTO(cacheDTO);
//        }
//
//        if (response == null) {
//            response = tripHighLightDAO.getAllTripHighLightByTrip(id);
//        }
//
//        if (response != null && response.getId() != 0) {
//            TripHighLightCacheDTO cacheDTO = convertToCacheDTO(response);
//            putTripHighLightCache(cacheKey, cacheDTO);
//        }
//
//        return response;
//    }
//    public List<TripHighLightCacheDTO> getAllTripHighLightFromCache(int id) {
//    	List<TripHighLightCacheDTO> response = new ArrayList<TripHighLightCacheDTO>();
//        String cacheKey = "TRIP_HIGHLIGHT_" + id; 
//
//        ValueWrapper wrapper = cacheManager.getCache(TRIP_HIGHLIGHT_CACHE_NAME).get(cacheKey);
//        if (wrapper != null && wrapper.get() != null) {
//            TripHighLightCacheDTO cacheDTO = (TripHighLightCacheDTO) wrapper.get();
//            response = convertToTripHighLightDTO(cacheDTO);
//        }
//
//        if (response == null) {
//            response = tripHighLightDAO.getAllTripHighLightByTrip(id);
//        }
//
//        if (response != null && response.getId() != 0) {
//            TripHighLightCacheDTO cacheDTO = convertToCacheDTO(response);
//            putTripHighLightCache(cacheKey, cacheDTO);
//        }
//
//        return response;
//    }
//
//    @Override
//    public void putTripHighLightCache(String cacheKey, TripHighLightCacheDTO cacheDTO) {
//        Cache cache = cacheManager.getCache(TRIP_HIGHLIGHT_CACHE_NAME);
//        if (cache != null) {
//            cache.put(cacheKey, cacheDTO);
//        }
//    }
//
//    private TripHighLightCacheDTO convertToCacheDTO(TripHighLightDTO dto) {
//        TripHighLightCacheDTO cacheDTO = new TripHighLightCacheDTO();
//        cacheDTO.setId(dto.getId());
//        cacheDTO.setName(dto.getName());
//        cacheDTO.setCode(dto.getCode());
//        cacheDTO.setTripDTO(dto.getTripDTO());
//        cacheDTO.setHighLight(dto.getHighLight());
//        cacheDTO.setActiveFlag(dto.getActiveFlag());
//        cacheDTO.setUpdatedBy(dto.getUpdatedBy());
//        cacheDTO.setUpdatedAt(dto.getUpdatedAt());
//        return cacheDTO;
//    }
//
//    private TripHighLightDTO convertToTripHighLightDTO(TripHighLightCacheDTO cacheDTO) {
//        TripHighLightDTO dto = new TripHighLightDTO();
//        dto.setId(cacheDTO.getId());
//        dto.setName(cacheDTO.getName());
//        dto.setCode(cacheDTO.getCode());
//        dto.setTripDTO(cacheDTO.getTripDTO());
//        dto.setHighLight(cacheDTO.getHighLight());
//        dto.setActiveFlag(cacheDTO.getActiveFlag());
//        dto.setUpdatedBy(cacheDTO.getUpdatedBy());
//        dto.setUpdatedAt(cacheDTO.getUpdatedAt());
//        return dto;
//    }
//}
//
