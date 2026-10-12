package com.example.summaryapp.model;

/** Holds the name, email, and summary submitted by a user. */
public class SubmissionForm {
    private String name;
    private String email;
    private String summary;

    public SubmissionForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
