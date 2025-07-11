package com.ezee.trip.cache;

import com.ezee.trip.cache.dto.TripCacheDTO;
import com.ezee.trip.dto.TripDTO;

public interface RedisTripService {
    public TripDTO getTripFromCache(TripDTO tripDTO);
    public void putTripCache(String cacheKey, TripCacheDTO cacheDTO);

}
