package org.example.repository;

import org.example.domain.Student;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcStudentRepositoryIT {

    private JdbcStudentRepository repository;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:it_" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        repository = new JdbcStudentRepository(dataSource);
        repository.deleteAll();
    }

    @Test
    void save_andFindByStudentNumber_roundTripWorks() {
        Student saved = repository.save(new Student(null, "20261001", "Ali Veli", "ali@example.com", 3.8));

        Optional<Student> found = repository.findByStudentNumber("20261001");

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Ali Veli", found.get().getFullName());
    }

    @Test
    void findByEmail_returnsSavedStudent() {
        Student saved = repository.save(new Student(null, "20261011", "Nisa", "nisa@example.com", 3.2));

        Optional<Student> found = repository.findByEmail("nisa@example.com");

        assertTrue(found.isPresent());
        assertEquals(saved.getStudentNumber(), found.get().getStudentNumber());
    }

    @Test
    void update_changesPersistedFields() {
        Student saved = repository.save(new Student(null, "20261012", "Mert", "mert@example.com", 2.6));
        Student updatedInput = new Student(saved.getId(), saved.getStudentNumber(), "Mert Updated", "mert.u@example.com", 3.0);

        repository.update(updatedInput);

        Optional<Student> loaded = repository.findByStudentNumber("20261012");
        assertTrue(loaded.isPresent());
        assertEquals("Mert Updated", loaded.get().getFullName());
        assertEquals("mert.u@example.com", loaded.get().getEmail());
        assertEquals(3.0, loaded.get().getGpa());
    }

    @Test
    void findAll_returnsRecordsOrderedById() {
        repository.save(new Student(null, "20261002", "Bir", "bir@example.com", 2.5));
        repository.save(new Student(null, "20261003", "Iki", "iki@example.com", 3.1));

        List<Student> students = repository.findAll();

        assertEquals(2, students.size());
        assertEquals("20261002", students.get(0).getStudentNumber());
        assertEquals("20261003", students.get(1).getStudentNumber());
    }

    @Test
    void findByStudentNumber_whenNotExists_returnsEmpty() {
        Optional<Student> result = repository.findByStudentNumber("404");

        assertTrue(result.isEmpty());
    }
}
