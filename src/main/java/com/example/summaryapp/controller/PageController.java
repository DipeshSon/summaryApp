package com.example.summaryapp.controller;

import com.example.summaryapp.model.SubmissionForm;
import com.example.summaryapp.service.ValidationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PageController {
    private final ValidationService validationService;

    public PageController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/about.html";
    }

    @PostMapping("/verify")
    public String verify(
            @ModelAttribute("submission") SubmissionForm submission,
            Model model,
            HttpSession session) {
        List<String> errors = validationService.validate(submission);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("submission", submission);
            return "verify";
        }

        session.setAttribute("submission", submission);
        return "redirect:/display";
    }

    @GetMapping("/display")
    public String display(HttpSession session, Model model) {
        Object submission = session.getAttribute("submission");
        if (submission == null) {
            return "redirect:/about.html";
        }

        model.addAttribute("submission", submission);
        return "display";
    }
}
