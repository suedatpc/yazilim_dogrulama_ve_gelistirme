package org.example.service;

public record StudentStatistics(
        int totalStudents,
        int honorStudents,
        int probationStudents,
        double averageGpa
) {
}

