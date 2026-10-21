package org.example.service;

import org.example.domain.AcademicStanding;
import org.example.domain.Student;
import org.example.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    @Test
    void registerStudent_whenRequestValid_savesAndReturnsStudent() {
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "20260010",
                "Can Demir",
                "can@example.com",
                3.5
        );
        Student persisted = new Student(1L, "20260010", "Can Demir", "can@example.com", 3.5);
        when(repository.save(any(Student.class))).thenReturn(persisted);

        Student result = service.registerStudent(request);

        assertEquals(1L, result.getId());
        assertEquals("20260010", result.getStudentNumber());
        assertEquals("can@example.com", result.getEmail());
        verify(repository).save(any(Student.class));
    }

    @Test
    void registerStudent_normalizesInputBeforeSave() {
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "20260013",
                "  Ada Lovelace  ",
                "  ADA@EXAMPLE.COM  ",
                3.8
        );
        when(repository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.registerStudent(request);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(repository).save(captor.capture());
        assertEquals("Ada Lovelace", captor.getValue().getFullName());
        assertEquals("ada@example.com", captor.getValue().getEmail());
    }

    @Test
    void registerStudent_whenEmailInvalid_throwsException() {
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "20260011",
                "Derya Ucar",
                "invalid-email",
                3.0
        );

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.registerStudent(request));

        assertEquals("Email format is invalid", error.getMessage());
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void getByStudentNumber_delegatesToRepository() {
        Student student = new Student(2L, "20260012", "Ece Nur", "ece@example.com", 3.1);
        when(repository.findByStudentNumber("20260012")).thenReturn(Optional.of(student));

        Optional<Student> found = service.getByStudentNumber("20260012");

        assertTrue(found.isPresent());
        assertEquals("Ece Nur", found.get().getFullName());
    }

    @Test
    void listHonorStudents_filtersByMinimumGpa() {
        when(repository.findAll()).thenReturn(List.of(
                new Student(1L, "20260001", "A", "a@example.com", 3.9),
                new Student(2L, "20260002", "B", "b@example.com", 3.2),
                new Student(3L, "20260003", "C", "c@example.com", 2.7)
        ));

        List<Student> honorStudents = service.listHonorStudents(3.5);

        assertEquals(1, honorStudents.size());
        assertEquals("20260001", honorStudents.get(0).getStudentNumber());
    }

    @Test
    void updateStudent_whenStudentExists_updatesFields() {
        Student existing = new Student(10L, "20260099", "Old Name", "old@example.com", 2.5);
        when(repository.findByStudentNumber("20260099")).thenReturn(Optional.of(existing));
        when(repository.update(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = service.updateStudent("20260099", new StudentUpdateRequest("New Name", "new@example.com", 3.1));

        assertEquals(10L, updated.getId());
        assertEquals("New Name", updated.getFullName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(3.1, updated.getGpa());
    }

    @Test
    void searchStudents_filtersByDomainAndStanding() {
        when(repository.findAll()).thenReturn(List.of(
                new Student(1L, "20260101", "Ali", "ali@campus.edu", 3.6),
                new Student(2L, "20260102", "Veli", "veli@campus.edu", 3.1),
                new Student(3L, "20260103", "Ayse", "ayse@example.com", 3.9)
        ));

        List<Student> result = service.searchStudents(new StudentSearchCriteria(null, "campus.edu", null, null, AcademicStanding.GOOD));

        assertEquals(1, result.size());
        assertEquals("20260102", result.get(0).getStudentNumber());
    }

    @Test
    void calculateStatistics_returnsAggregatedValues() {
        when(repository.findAll()).thenReturn(List.of(
                new Student(1L, "20260111", "A", "a@a.com", 3.8),
                new Student(2L, "20260112", "B", "b@b.com", 2.4),
                new Student(3L, "20260113", "C", "c@c.com", 1.7)
        ));

        StudentStatistics statistics = service.calculateStatistics();

        assertEquals(3, statistics.totalStudents());
        assertEquals(1, statistics.honorStudents());
        assertEquals(1, statistics.probationStudents());
        assertEquals(2.6333333333333333, statistics.averageGpa());
    }
}
