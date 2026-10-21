package org.example.service;

public record StudentUpdateRequest(
        String fullName,
        String email,
        double gpa
) {
}

