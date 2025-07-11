package com.ezee.trip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezee.trip.controller.io.AuthIO;
import com.ezee.trip.controller.io.ResponseIO;
import com.ezee.trip.dto.AuthDTO;
import com.ezee.trip.dto.AuthResponseDTO;
import com.ezee.trip.service.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/token")
	public ResponseIO<AuthResponseDTO> generateToken(@RequestBody AuthIO authIO) {
		AuthDTO auth = new AuthDTO();
		auth.setUsername(authIO.getUsername());
		auth.setPassword(authIO.getPassword());
		AuthResponseDTO authToken = authService.authToken(auth);
		return ResponseIO.success(authToken);
	}
}

