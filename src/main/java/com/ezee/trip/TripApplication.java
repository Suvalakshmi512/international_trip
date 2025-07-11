package com.ezee.trip;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TripApplication {
	public static final Logger LOGGER = LogManager.getLogger("com.ezee.trip.MainApp");

	public static void main(String[] args) {
		try {
			SpringApplication.run(TripApplication.class, args);
		} catch (Exception e) {
			LOGGER.error("Application failed to start: " + e.getMessage());
		}
	}
}
