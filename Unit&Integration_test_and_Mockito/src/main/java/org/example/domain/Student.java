package org.example.domain;

import java.util.Objects;

public final class Student {
    private final Long id;
    private final String studentNumber;
    private final String fullName;
    private final String email;
    private final double gpa;

    public Student(Long id, String studentNumber, String fullName, String email, double gpa) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.fullName = fullName;
        this.email = email;
        this.gpa = gpa;
    }

    public Long getId() {
        return id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student student)) {
            return false;
        }
        return Double.compare(student.gpa, gpa) == 0
                && Objects.equals(id, student.id)
                && Objects.equals(studentNumber, student.studentNumber)
                && Objects.equals(fullName, student.fullName)
                && Objects.equals(email, student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentNumber, fullName, email, gpa);
    }
}

