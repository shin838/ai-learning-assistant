package com.example.ailearningassistant.health;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping("/api/health")
	public HealthCheckResponse health() {
		return new HealthCheckResponse("ok", Instant.now());
	}
}
