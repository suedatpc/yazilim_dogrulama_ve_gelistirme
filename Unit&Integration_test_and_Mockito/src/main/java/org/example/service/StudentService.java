package org.example.service;

import org.example.domain.AcademicStanding;
import org.example.domain.Student;
import org.example.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student registerStudent(StudentRegistrationRequest request) {
        validateRegistration(request);
        Student student = new Student(
                null,
                request.studentNumber().trim(),
                request.fullName().trim(),
                request.email().trim().toLowerCase(Locale.ROOT),
                request.gpa()
        );
        return repository.save(student);
    }

    public Optional<Student> getByStudentNumber(String studentNumber) {
        return repository.findByStudentNumber(studentNumber);
    }

    public Optional<Student> getByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return repository.findByEmail(email.trim().toLowerCase(Locale.ROOT));
    }

    public List<Student> listHonorStudents(double minGpa) {
        return repository.findAll().stream()
                .filter(student -> student.getGpa() >= minGpa)
                .toList();
    }

    public List<Student> listProbationStudents() {
        return repository.findAll().stream()
                .filter(student -> getAcademicStanding(student.getGpa()) == AcademicStanding.PROBATION)
                .toList();
    }

    public Student updateStudent(String studentNumber, StudentUpdateRequest request) {
        validateUpdate(studentNumber, request);
        Student currentStudent = repository.findByStudentNumber(studentNumber.trim())
                .orElseThrow(() -> new IllegalArgumentException("Student was not found"));

        Student updatedStudent = new Student(
                currentStudent.getId(),
                currentStudent.getStudentNumber(),
                request.fullName().trim(),
                request.email().trim().toLowerCase(Locale.ROOT),
                request.gpa()
        );
        return repository.update(updatedStudent);
    }

    public List<Student> searchStudents(StudentSearchCriteria criteria) {
        if (criteria == null) {
            return repository.findAll();
        }

        return repository.findAll().stream()
                .filter(student -> filterByName(criteria, student))
                .filter(student -> filterByDomain(criteria, student))
                .filter(student -> filterByMinGpa(criteria, student))
                .filter(student -> filterByMaxGpa(criteria, student))
                .filter(student -> filterByStanding(criteria, student))
                .sorted(Comparator.comparing(Student::getStudentNumber))
                .toList();
    }

    public StudentStatistics calculateStatistics() {
        List<Student> students = repository.findAll();
        int total = students.size();
        int honor = (int) students.stream()
                .filter(student -> getAcademicStanding(student.getGpa()) == AcademicStanding.HONOR)
                .count();
        int probation = (int) students.stream()
                .filter(student -> getAcademicStanding(student.getGpa()) == AcademicStanding.PROBATION)
                .count();
        double average = students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);

        return new StudentStatistics(total, honor, probation, average);
    }

    public AcademicStanding getAcademicStanding(double gpa) {
        if (gpa >= 3.5) {
            return AcademicStanding.HONOR;
        }
        if (gpa < 2.0) {
            return AcademicStanding.PROBATION;
        }
        return AcademicStanding.GOOD;
    }

    private boolean filterByName(StudentSearchCriteria criteria, Student student) {
        if (criteria.nameContains() == null || criteria.nameContains().isBlank()) {
            return true;
        }
        return student.getFullName().toLowerCase(Locale.ROOT)
                .contains(criteria.nameContains().trim().toLowerCase(Locale.ROOT));
    }

    private boolean filterByDomain(StudentSearchCriteria criteria, Student student) {
        if (criteria.emailDomain() == null || criteria.emailDomain().isBlank()) {
            return true;
        }
        String domain = "@" + criteria.emailDomain().trim().toLowerCase(Locale.ROOT);
        return student.getEmail().toLowerCase(Locale.ROOT).endsWith(domain);
    }

    private boolean filterByMinGpa(StudentSearchCriteria criteria, Student student) {
        return criteria.minGpa() == null || student.getGpa() >= criteria.minGpa();
    }

    private boolean filterByMaxGpa(StudentSearchCriteria criteria, Student student) {
        return criteria.maxGpa() == null || student.getGpa() <= criteria.maxGpa();
    }

    private boolean filterByStanding(StudentSearchCriteria criteria, Student student) {
        return criteria.standing() == null || getAcademicStanding(student.getGpa()) == criteria.standing();
    }

    private void validateRegistration(StudentRegistrationRequest request) {
        if (request.studentNumber() == null || request.studentNumber().isBlank()) {
            throw new IllegalArgumentException("Student number cannot be blank");
        }
        validateNameEmailGpa(request.fullName(), request.email(), request.gpa());
    }

    private void validateUpdate(String studentNumber, StudentUpdateRequest request) {
        if (studentNumber == null || studentNumber.isBlank()) {
            throw new IllegalArgumentException("Student number cannot be blank");
        }
        validateNameEmailGpa(request.fullName(), request.email(), request.gpa());
    }

    private void validateNameEmailGpa(String fullName, String email, double gpa) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be blank");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email format is invalid");
        }
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
    }
}
