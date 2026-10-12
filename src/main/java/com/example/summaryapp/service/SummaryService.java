package com.example.summaryapp.service;

import com.example.summaryapp.model.SummaryForm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// Keep the validation rules in the service instead of the controller or JSPs.
@Service
public class SummaryService {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Z][a-z]{1,8}$");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9]+([._][A-Za-z0-9]+)*@gmail\\.com$");
    private static final Pattern ENGLISH_LETTERS_PATTERN = Pattern.compile("^[A-Za-z]+$");
    private static final Pattern FIRST_UPPERCASE_LETTER_PATTERN = Pattern.compile("^[A-Z]");
    private static final Pattern UPPERCASE_LETTER_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOCAL_PART_PATTERN =
            Pattern.compile("^[A-Za-z0-9]+([._][A-Za-z0-9]+)*$");

    // Trim surrounding whitespace, then require an uppercase English letter and one to eight lowercase letters.
    public boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    // Trim surrounding whitespace and apply the complete Gmail address pattern.
    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String trimmedEmail = email.trim();
        return hasExactlyOneAt(trimmedEmail)
                && hasGmailSuffixOnlyAtEnd(trimmedEmail)
                && endsWithGmail(trimmedEmail)
                && !hasEmptyLocalPart(trimmedEmail)
                && EMAIL_PATTERN.matcher(trimmedEmail).matches();
    }

    // Return a specific validation message for each failed rule.
    public List<String> validate(SummaryForm form) {
        if (form == null) {
            return List.of("Name must start with an uppercase letter",
                    "Email must contain exactly one @");
        }

        List<String> errors = new ArrayList<>();
        errors.addAll(validateName(form.getName()));
        errors.addAll(validateEmail(form.getEmail()));
        return errors;
    }

    // Check the name rules independently so every failure has a useful message.
    private List<String> validateName(String submittedName) {
        List<String> errors = new ArrayList<>();
        String name = submittedName == null ? "" : submittedName.trim();

        if (name.isEmpty() || !startsWithUppercase(name)) {
            errors.add("Name must start with an uppercase letter");
        }
        if (isNameTooLong(name)) {
            errors.add("Name must be fewer than 10 characters");
        } else if (isNameTooShort(name)) {
            errors.add("Name must contain 2 to 9 letters");
        }
        if (!name.isEmpty() && !containsOnlyEnglishLetters(name)) {
            errors.add("Name must contain only letters");
        }
        if (hasMixedNameCase(name)) {
            errors.add("Name must not be in camelCase; use one capital letter followed by lowercase letters");
        }
        return errors;
    }

    // Check the email rules independently so each failure is reported specifically.
    private List<String> validateEmail(String submittedEmail) {
        List<String> errors = new ArrayList<>();
        String email = submittedEmail == null ? "" : submittedEmail.trim();

        if (!hasExactlyOneAt(email)) {
            errors.add("Email must contain exactly one @");
        }
        if (hasGmailSuffixInWrongPosition(email)) {
            errors.add("@gmail.com must appear only once, at the end of the email");
        } else if (!endsWithGmail(email)) {
            errors.add("Email must end with @gmail.com");
        }
        if (endsWithGmail(email) && hasEmptyLocalPart(email)) {
            errors.add("Email must not be empty before @gmail.com");
        }

        String localPart = getLocalPart(email);
        if (hasExactlyOneAt(email) && hasConsecutiveSeparators(localPart)) {
            errors.add("Email must not contain consecutive dots or underscores");
        } else if (hasExactlyOneAt(email) && hasInvalidLocalPartEdge(localPart)) {
            errors.add("Email must not start or end with a dot or underscore");
        } else if (hasExactlyOneAt(email) && !localPart.isEmpty()
                && !LOCAL_PART_PATTERN.matcher(localPart).matches()) {
            errors.add("Email local part must use letters, digits, dots, or underscores and start and end with a letter or digit");
        }
        return errors;
    }

    // The name must contain no more than nine characters.
    private boolean isNameTooLong(String name) {
        return name.length() > 9;
    }

    // The specified name pattern requires at least two characters.
    private boolean isNameTooShort(String name) {
        return !name.isEmpty() && name.length() < 2;
    }

    // The first name character must be uppercase.
    private boolean startsWithUppercase(String name) {
        return FIRST_UPPERCASE_LETTER_PATTERN.matcher(name).find();
    }

    // Every name character must be an ASCII English letter.
    private boolean containsOnlyEnglishLetters(String name) {
        return ENGLISH_LETTERS_PATTERN.matcher(name).matches();
    }

    // After the first capital, all name characters must be lowercase.
    private boolean hasMixedNameCase(String name) {
        return name.length() >= 2
                && containsOnlyEnglishLetters(name)
                && UPPERCASE_LETTER_PATTERN.matcher(name.substring(1)).find();
    }

    // The address must contain one and only one at sign.
    private boolean hasExactlyOneAt(String email) {
        return countOccurrences(email, "@") == 1;
    }

    // The Gmail suffix must occur once and only at the end.
    private boolean hasGmailSuffixOnlyAtEnd(String email) {
        return countOccurrences(email, "@gmail.com") == 1 && endsWithGmail(email);
    }

    // The domain must use the exact lowercase Gmail suffix.
    private boolean endsWithGmail(String email) {
        return email.endsWith("@gmail.com");
    }

    // The part before @gmail.com must contain at least one character.
    private boolean hasEmptyLocalPart(String email) {
        return email.indexOf('@') == 0;
    }

    // A repeated or non-final Gmail suffix has its own diagnostic.
    private boolean hasGmailSuffixInWrongPosition(String email) {
        int suffixCount = countOccurrences(email, "@gmail.com");
        return suffixCount > 1 || (suffixCount == 1 && !endsWithGmail(email));
    }

    // Return the text before the first at sign for local-part checks.
    private String getLocalPart(String email) {
        int atIndex = email.indexOf('@');
        return atIndex < 0 ? "" : email.substring(0, atIndex);
    }

    // Count literal matches so repeated @ signs and Gmail suffixes have distinct errors.
    private int countOccurrences(String value, String target) {
        int count = 0;
        int index = 0;
        while ((index = value.indexOf(target, index)) != -1) {
            count++;
            index += target.length();
        }
        return count;
    }

    // Adjacent dots or underscores are not allowed as local-part separators.
    private boolean hasConsecutiveSeparators(String localPart) {
        return localPart.contains("..")
                || localPart.contains("__")
                || localPart.contains("._")
                || localPart.contains("_.");
    }

    // The local part must begin and end with a letter or digit.
    private boolean hasInvalidLocalPartEdge(String localPart) {
        return !localPart.isEmpty()
                && (isSeparator(localPart.charAt(0))
                || isSeparator(localPart.charAt(localPart.length() - 1)));
    }

    // Dots and underscores cannot be the first or last local-part characters.
    private boolean isSeparator(char character) {
        return character == '.' || character == '_';
    }
}
