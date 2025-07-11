package com.ezee.trip.cache;

import com.ezee.trip.cache.dto.TripHighLightCacheDTO;
import com.ezee.trip.dto.TripHighLightDTO;

public interface RedisTripHighLightService {
    public TripHighLightDTO getTripHighLightFromCache(int id);
    public void putTripHighLightCache(String cacheKey, TripHighLightCacheDTO cacheDTO);

}
