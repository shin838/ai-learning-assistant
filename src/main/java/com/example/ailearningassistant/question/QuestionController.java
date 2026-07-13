package com.example.ailearningassistant.question;

import com.example.ailearningassistant.openai.OpenAiApiException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QuestionController {

	private final QuestionGenerationService questionGenerationService;

	public QuestionController(QuestionGenerationService questionGenerationService) {
		this.questionGenerationService = questionGenerationService;
	}

	@PostMapping("/questions/generate")
	public String generate(@ModelAttribute QuestionGenerationRequest request, Model model) {
		if (request.examScope() == null || request.examScope().isBlank()) {
			model.addAttribute("errorMessage", "시험 범위를 입력해주세요.");
			return "index";
		}

		try {
			QuestionGenerationResult result = questionGenerationService.generate(request);
			model.addAttribute("result", result);
			return "result";
		}
		catch (OpenAiApiException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			model.addAttribute("examScope", request.examScope());
			return "index";
		}
	}
}
