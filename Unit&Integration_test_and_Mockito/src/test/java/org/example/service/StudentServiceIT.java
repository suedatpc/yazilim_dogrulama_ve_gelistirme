package org.example.service;

import org.example.domain.AcademicStanding;
import org.example.domain.Student;
import org.example.repository.JdbcStudentRepository;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentServiceIT {

    private StudentService service;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:service_it;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        JdbcStudentRepository repository = new JdbcStudentRepository(dataSource);
        repository.deleteAll();
        service = new StudentService(repository);
    }

    @Test
    void registerStudent_andHonorList_flowWorksEndToEnd() {
        service.registerStudent(new StudentRegistrationRequest("20261101", "Gizem", "gizem@example.com", 3.6));
        service.registerStudent(new StudentRegistrationRequest("20261102", "Bora", "bora@example.com", 2.8));

        List<Student> honorStudents = service.listHonorStudents(3.5);

        assertEquals(1, honorStudents.size());
        assertEquals("20261101", honorStudents.get(0).getStudentNumber());
    }

    @Test
    void update_andSearch_flowWorksEndToEnd() {
        service.registerStudent(new StudentRegistrationRequest("20261103", "Mina", "mina@campus.edu", 2.9));

        service.updateStudent("20261103", new StudentUpdateRequest("Mina T", "mina.t@campus.edu", 3.2));

        List<Student> result = service.searchStudents(
                new StudentSearchCriteria("mina", "campus.edu", 3.0, null, AcademicStanding.GOOD)
        );

        assertEquals(1, result.size());
        assertEquals("Mina T", result.get(0).getFullName());
        assertEquals("mina.t@campus.edu", result.get(0).getEmail());
    }

    @Test
    void calculateStatistics_returnsExpectedDistribution() {
        service.registerStudent(new StudentRegistrationRequest("20261104", "A", "a@a.com", 3.9));
        service.registerStudent(new StudentRegistrationRequest("20261105", "B", "b@b.com", 2.4));
        service.registerStudent(new StudentRegistrationRequest("20261106", "C", "c@c.com", 1.5));

        StudentStatistics statistics = service.calculateStatistics();

        assertEquals(3, statistics.totalStudents());
        assertEquals(1, statistics.honorStudents());
        assertEquals(1, statistics.probationStudents());
    }
}
