package org.example.repository;

import org.example.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);

    Optional<Student> findByStudentNumber(String studentNumber);

    // Yeni akislar icin ek sorgu metotlari.
    Optional<Student> findByEmail(String email);

    List<Student> findAll();

    Student update(Student student);

    void deleteAll();
}
