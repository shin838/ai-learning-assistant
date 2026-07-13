package com.example.ailearningassistant.question;

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
			return "redirect:/";
		}

		QuestionGenerationResult result = questionGenerationService.generate(request);
		model.addAttribute("result", result);
		return "result";
	}
}
