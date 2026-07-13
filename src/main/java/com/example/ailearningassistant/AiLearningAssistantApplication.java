package com.example.ailearningassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AiLearningAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiLearningAssistantApplication.class, args);
	}
}
