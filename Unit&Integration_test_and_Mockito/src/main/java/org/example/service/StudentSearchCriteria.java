package org.example.service;

import org.example.domain.AcademicStanding;

public record StudentSearchCriteria(
        String nameContains,
        String emailDomain,
        Double minGpa,
        Double maxGpa,
        AcademicStanding standing
) {
}

