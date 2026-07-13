package com.example.ailearningassistant.question;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QuestionController {

	private final QuestionGenerationService questionGenerationService;
	private final ExamScopeValidator examScopeValidator;

	public QuestionController(
		QuestionGenerationService questionGenerationService,
		ExamScopeValidator examScopeValidator
	) {
		this.questionGenerationService = questionGenerationService;
		this.examScopeValidator = examScopeValidator;
	}

	@PostMapping("/questions/generate")
	public String generate(@ModelAttribute QuestionGenerationRequest request, Model model) {
		String examScope = examScopeValidator.normalize(request.examScope());
		var validationError = examScopeValidator.validate(examScope);
		if (validationError.isPresent()) {
			model.addAttribute("errorMessage", validationError.get());
			model.addAttribute("examScope", examScope);
			return "index";
		}

		QuestionGenerationResult result = questionGenerationService.generate(new QuestionGenerationRequest(examScope));
		model.addAttribute("result", result);
		return "result";
	}
}
