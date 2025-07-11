package com.ezee.trip.cache;

import com.ezee.trip.cache.dto.AvailableDateCacheDTO;
import com.ezee.trip.dto.AvailableDateDTO;

public interface RedisAvailableDateService {
    public AvailableDateDTO getAvailableDateFromCache(int id);
    public void putAvailableDateCache(String cacheKey, AvailableDateCacheDTO cacheDTO);

}
