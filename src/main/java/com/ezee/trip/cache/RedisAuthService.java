package com.ezee.trip.cache;

import com.ezee.trip.dto.AuthResponseDTO;

public interface RedisAuthService {
	public AuthResponseDTO getAuthByUsername(String username);

	public AuthResponseDTO getAuthByToken(String token);

	public void putAuth(AuthResponseDTO dto);

	public void clearAuth(String key);
}
