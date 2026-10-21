package org.example.repository;

import org.example.domain.Student;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStudentRepository implements StudentRepository {
    private final DataSource dataSource;

    public JdbcStudentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        createTableIfNotExists();
    }

    @Override
    public Student save(Student student) {
        String sql = "INSERT INTO students (student_number, full_name, email, gpa) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getStudentNumber());
            statement.setString(2, student.getFullName());
            statement.setString(3, student.getEmail());
            statement.setDouble(4, student.getGpa());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    return new Student(id, student.getStudentNumber(), student.getFullName(), student.getEmail(), student.getGpa());
                }
            }
            throw new IllegalStateException("Generated key was not returned by database");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save student", e);
        }
    }

    @Override
    public Optional<Student> findByStudentNumber(String studentNumber) {
        String sql = "SELECT id, student_number, full_name, email, gpa FROM students WHERE student_number = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentNumber);
            return executeSingleResult(statement);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find student by number", e);
        }
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        String sql = "SELECT id, student_number, full_name, email, gpa FROM students WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            return executeSingleResult(statement);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find student by email", e);
        }
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT id, student_number, full_name, email, gpa FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapRow(resultSet));
            }
            return students;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find all students", e);
        }
    }

    @Override
    public Student update(Student student) {
        if (student.getId() == null) {
            throw new IllegalArgumentException("Student id cannot be null for update");
        }

        String sql = "UPDATE students SET full_name = ?, email = ?, gpa = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getFullName());
            statement.setString(2, student.getEmail());
            statement.setDouble(3, student.getGpa());
            statement.setLong(4, student.getId());

            int updated = statement.executeUpdate();
            if (updated == 0) {
                throw new IllegalStateException("No row updated for id=" + student.getId());
            }
            return student;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update student", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM students";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to clear students table", e);
        }
    }

    private Optional<Student> executeSingleResult(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(mapRow(resultSet));
            }
        }
        return Optional.empty();
    }

    private void createTableIfNotExists() {
        String ddl = """
                CREATE TABLE IF NOT EXISTS students (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    student_number VARCHAR(32) NOT NULL UNIQUE,
                    full_name VARCHAR(128) NOT NULL,
                    email VARCHAR(128) NOT NULL,
                    gpa DOUBLE NOT NULL
                )
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(ddl)) {
            statement.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create students table", e);
        }
    }

    private Student mapRow(ResultSet resultSet) throws SQLException {
        return new Student(
                resultSet.getLong("id"),
                resultSet.getString("student_number"),
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getDouble("gpa")
        );
    }
}
