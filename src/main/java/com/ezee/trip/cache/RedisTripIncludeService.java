package com.ezee.trip.cache;

import com.ezee.trip.cache.dto.TripIncludeCacheDTO;
import com.ezee.trip.dto.TripIncludeDTO;

public interface RedisTripIncludeService {
    public TripIncludeDTO getTripIncludeFromCache(int id);
    public void putTripIncludeCache(String cacheKey, TripIncludeCacheDTO cacheDTO);
    


}
