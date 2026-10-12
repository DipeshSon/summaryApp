package com.example.summaryapp.service;

import com.example.summaryapp.model.SummaryForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SummaryServiceTest {
    private final SummaryService summaryService = new SummaryService();

    @ParameterizedTest
    @MethodSource("validNames")
    void acceptsValidNames(String name) {
        assertTrue(summaryService.isValidName(name));
        assertTrue(summaryService.validate(form(name, "rahul@gmail.com")).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    void rejectsInvalidNamesWithSpecificMessages(String name, List<String> expectedErrors) {
        assertFalse(summaryService.isValidName(name));
        assertEquals(expectedErrors, summaryService.validate(form(name, "rahul@gmail.com")));
    }

    @ParameterizedTest
    @MethodSource("validEmails")
    void acceptsValidEmails(String email) {
        assertTrue(summaryService.isValidEmail(email));
        assertTrue(summaryService.validate(form("Rahul", email)).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidEmails")
    void rejectsInvalidEmailsWithSpecificMessages(String email, List<String> expectedErrors) {
        assertFalse(summaryService.isValidEmail(email));
        assertEquals(expectedErrors, summaryService.validate(form("Rahul", email)));
    }

    @Test
    void trimsInputsBeforeValidation() {
        assertTrue(summaryService.isValidName("  Rahul  "));
        assertTrue(summaryService.isValidEmail("  rahul@gmail.com  "));
    }

    private static Stream<Arguments> validNames() {
        return Stream.of("Rahul", "Anita", "Ravi", "Li", "Venkatesh")
                .map(Arguments::of);
    }

    private static Stream<Arguments> invalidNames() {
        return Stream.of(
                Arguments.of("rahul", List.of("Name must start with an uppercase letter")),
                Arguments.of("rAhul", List.of(
                        "Name must start with an uppercase letter",
                        "Name must not be in camelCase; use one capital letter followed by lowercase letters")),
                Arguments.of("RaHuL", List.of(
                        "Name must not be in camelCase; use one capital letter followed by lowercase letters")),
                Arguments.of("RahulKumar", List.of(
                        "Name must be fewer than 10 characters",
                        "Name must not be in camelCase; use one capital letter followed by lowercase letters")),
                Arguments.of("Venkatesh1", List.of(
                        "Name must be fewer than 10 characters",
                        "Name must contain only letters")),
                Arguments.of("Rahul Raj", List.of("Name must contain only letters")),
                Arguments.of("Émilie", List.of(
                        "Name must start with an uppercase letter",
                        "Name must contain only letters")),
                Arguments.of("Venkateshh", List.of("Name must be fewer than 10 characters")),
                Arguments.of("R", List.of("Name must contain 2 to 9 letters")),
                Arguments.of("", List.of("Name must start with an uppercase letter")),
                Arguments.of(null, List.of("Name must start with an uppercase letter")),
                Arguments.of("   ", List.of("Name must start with an uppercase letter"))
        );
    }

    private static Stream<Arguments> validEmails() {
        return Stream.of(
                "rahul@gmail.com",
                "rahul.k@gmail.com",
                "rahul_k1@gmail.com",
                "a1@gmail.com"
        ).map(Arguments::of);
    }

    private static Stream<Arguments> invalidEmails() {
        return Stream.of(
                Arguments.of("@gmail.comrahul", List.of(
                        "@gmail.com must appear only once, at the end of the email")),
                Arguments.of("rahul@gmail.com@gmail.com", List.of(
                        "Email must contain exactly one @",
                        "@gmail.com must appear only once, at the end of the email")),
                Arguments.of("rahul@@gmail.com", List.of(
                        "Email must contain exactly one @")),
                Arguments.of("rahul@gmail.comm", List.of(
                        "@gmail.com must appear only once, at the end of the email")),
                Arguments.of("rahul@yahoo.com", List.of("Email must end with @gmail.com")),
                Arguments.of("rahul..k@gmail.com", List.of(
                        "Email must not contain consecutive dots or underscores")),
                Arguments.of("rahul__k@gmail.com", List.of(
                        "Email must not contain consecutive dots or underscores")),
                Arguments.of(".rahul@gmail.com", List.of(
                        "Email must not start or end with a dot or underscore")),
                Arguments.of("rahul.@gmail.com", List.of(
                        "Email must not start or end with a dot or underscore")),
                Arguments.of("érahul@gmail.com", List.of(
                        "Email local part must use letters, digits, dots, or underscores and start and end with a letter or digit")),
                Arguments.of("@gmail.com", List.of(
                        "Email must not be empty before @gmail.com")),
                Arguments.of("", List.of(
                        "Email must contain exactly one @",
                        "Email must end with @gmail.com")),
                Arguments.of(null, List.of(
                        "Email must contain exactly one @",
                        "Email must end with @gmail.com")),
                Arguments.of("   ", List.of(
                        "Email must contain exactly one @",
                        "Email must end with @gmail.com"))
        );
    }

    private static SummaryForm form(String name, String email) {
        SummaryForm form = new SummaryForm();
        form.setName(name);
        form.setEmail(email);
        return form;
    }
}
