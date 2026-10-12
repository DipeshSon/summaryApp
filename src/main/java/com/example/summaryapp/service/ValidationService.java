package com.example.summaryapp.service;

import com.example.summaryapp.model.SubmissionForm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationService {
    private final SummaryService summaryService;

    public ValidationService(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    public List<String> validate(SubmissionForm submission) {
        List<String> errors = new ArrayList<>();
        if (submission == null) {
            return List.of("Submission is required");
        }
        if (!summaryService.isValidName(submission.getName())) {
            errors.add("Name must be valid");
        }
        if (!summaryService.isValidEmail(submission.getEmail())) {
            errors.add("Email must be valid");
        }
        if (submission.getSummary() == null || submission.getSummary().isBlank()) {
            errors.add("Summary is required");
        }
        return errors;
    }
}
