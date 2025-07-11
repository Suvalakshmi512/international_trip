package com.ezee.trip.service;

import com.ezee.trip.dto.AuthDTO;
import com.ezee.trip.dto.AuthResponseDTO;

public interface AuthService {
	
	public AuthResponseDTO authToken(AuthDTO authDTO);
	public AuthResponseDTO validateAuthCode(String authCode);

}
