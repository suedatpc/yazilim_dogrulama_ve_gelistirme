package org.example;

import org.example.domain.AcademicStanding;
import org.example.repository.JdbcStudentRepository;
import org.example.service.StudentRegistrationRequest;
import org.example.service.StudentSearchCriteria;
import org.example.service.StudentService;
import org.example.service.StudentStatistics;
import org.example.service.StudentUpdateRequest;
import org.h2.jdbcx.JdbcDataSource;

public class Main {
    public static void main(String[] args) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        StudentService service = new StudentService(new JdbcStudentRepository(dataSource));
        service.registerStudent(new StudentRegistrationRequest("20260001", "Ayse Yilmaz", "ayse@example.com", 3.72));
        service.registerStudent(new StudentRegistrationRequest("20260002", "Mehmet Kaya", "mehmet@campus.edu", 2.95));
        service.registerStudent(new StudentRegistrationRequest("20260003", "Elif Can", "elif@campus.edu", 1.85));

        service.updateStudent("20260002", new StudentUpdateRequest("Mehmet Kaya", "mehmet.kaya@campus.edu", 3.45));

        int honorCount = service.listHonorStudents(3.5).size();
        int probationCount = service.listProbationStudents().size();

        StudentSearchCriteria campusSearch = new StudentSearchCriteria(null, "campus.edu", 3.0, null, AcademicStanding.GOOD);
        int campusGoodCount = service.searchStudents(campusSearch).size();

        StudentStatistics statistics = service.calculateStatistics();

        System.out.println("Onur ogrencileri: " + honorCount);
        System.out.println("Probation ogrencileri: " + probationCount);
        System.out.println("Campus domain + GOOD ogrenci sayisi: " + campusGoodCount);
        System.out.println("Ortalama GPA: " + statistics.averageGpa());
    }
}