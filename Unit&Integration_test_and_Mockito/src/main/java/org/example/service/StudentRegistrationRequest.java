package org.example.service;

public record StudentRegistrationRequest(
        String studentNumber,
        String fullName,
        String email,
        double gpa
) {
}

