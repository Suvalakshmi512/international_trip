package com.ezee.trip.cache;

import com.ezee.trip.cache.dto.UserCacheDTO;
import com.ezee.trip.dto.UserDTO;

public interface RedisUserService {
    public UserDTO getUserFromCache(UserDTO userDTO);
    public void putUserCache(String cacheKey, UserCacheDTO cacheDTO);

}
