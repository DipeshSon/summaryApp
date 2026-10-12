package com.example.summaryapp.service;

import com.example.summaryapp.model.SubmissionForm;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationServiceTest {
    private final ValidationService validationService =
            new ValidationService(new SummaryService());

    @Test
    void acceptsValidSubmissions() {
        assertTrue(validationService.validate(form("Rahul", "rahul@gmail.com", "A summary")).isEmpty());
        assertTrue(validationService.validate(form("Anita", "a@gmail.com", "A summary")).isEmpty());
        assertTrue(validationService.validate(form("Kumar", "k1@gmail.com", "A summary")).isEmpty());
    }

    @Test
    void rejectsLowercaseName() {
        assertEquals(List.of("Name must be valid"),
                validationService.validate(form("rahul", "rahul@gmail.com", "A summary")));
    }

    @Test
    void rejectsNameThatIsTooLong() {
        assertEquals(List.of("Name must be valid"),
                validationService.validate(form("Venkatasubbu", "rahul@gmail.com", "A summary")));
    }

    @Test
    void rejectsNonGmailAddress() {
        assertEquals(List.of("Email must be valid"),
                validationService.validate(form("Ravi", "ravi@yahoo.com", "A summary")));
    }

    @Test
    void rejectsEmptyAndNullFields() {
        assertEquals(List.of(
                        "Name must be valid",
                        "Email must be valid",
                        "Summary is required"),
                validationService.validate(form("", "", "")));
        assertEquals(List.of(
                        "Name must be valid",
                        "Email must be valid",
                        "Summary is required"),
                validationService.validate(form(null, null, null)));
    }

    @Test
    void rejectsNullSubmission() {
        assertEquals(List.of("Submission is required"), validationService.validate(null));
    }

    private static SubmissionForm form(String name, String email, String summary) {
        SubmissionForm form = new SubmissionForm();
        form.setName(name);
        form.setEmail(email);
        form.setSummary(summary);
        return form;
    }
}
