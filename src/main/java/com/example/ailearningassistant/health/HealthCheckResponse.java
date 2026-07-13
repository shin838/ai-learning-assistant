package com.example.ailearningassistant.health;

import java.time.Instant;

public record HealthCheckResponse(
	String status,
	Instant checkedAt
) {
}
