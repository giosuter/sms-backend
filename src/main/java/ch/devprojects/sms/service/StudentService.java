package ch.devprojects.sms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ch.devprojects.sms.entity.Student;

/**
 * Service interface for managing student-related operations.
 * This interface provides methods to retrieve students based on different criteria.
 * 
 * Service Layer (Business Logic Layer)
 * ------------------------------------
 * Purpose:
 * -------
 * The service layer contains business logic and rules. It acts as an intermediary between 
 * the controller (handling HTTP requests) and the repository (handling database queries).
 */
public interface StudentService {

    /**
     * Retrieves all students from the database.
     * 
     * @return A list of all students.
     */
    List<Student> getAllStudents();

    /**
     * Retrieves a student by their unique ID.
     * 
     * @param id The ID of the student.
     * @return An Optional containing the student if found, otherwise empty.
     */
    Optional<Student> getStudentById(Long id);

    /**
     * Retrieves a student by their email.
     * 
     * @param email The email of the student.
     * @return An Optional containing the student if found, otherwise empty.
     */
    Optional<Student> getStudentByEmail(String email);

    /**
     * Retrieves a list of students by their first name.
     * 
     * @param firstName The first name of the student.
     * @return A list of students with the given first name.
     */
    List<Student> getStudentsByFirstName(String firstName);

    /**
     * Retrieves a list of students by their last name.
     * 
     * @param lastName The last name of the student.
     * @return A list of students with the given last name.
     */
    List<Student> getStudentsByLastName(String lastName);

    /**
     * Retrieves a list of students by their country.
     * 
     * @param country The country of the student.
     * @return A list of students from the specified country.
     */
    List<Student> getStudentsByCountry(String country);
    
    /**
     * Retrieves a list of students by their nationality.
     * 
     * @param nationality The nationality of the student.
     * @return A list of students with the specified nationality.
     */
    List<Student> getStudentsByNationality(String nationality);
    
    /**
     * Creates and saves a new student in the database.
     * 
     * @param student The student object to be saved.
     * @return The saved student object with generated ID.
     */
    Student createStudent(Student student);
    
    List<Student> createStudents(List<Student> students);

    Student registerStudent(Student student);
    
    /**
     * Updates an existing student based on the provided student ID.
     *
     * @param id The ID of the student to be updated.
     * @param updatedStudent The student object containing updated information.
     * @return The updated student entity.
     */
    Optional<Student> updateStudent(Long id, Student updatedStudent);
    
    /**
     * Updates specific fields of a student.
     * 
     * @param id The ID of the student to be updated.
     * @param updates A map containing the fields to be updated and their new values.
     * @return The updated student, or an empty optional if the student was not found.
     */
    Optional<Student> updateStudentFields(Long id, Map<String, Object> updates);
    
    /**
     * Deletes a student by their ID.
     * 
     * @param id The unique ID of the student to be deleted.
     */
    void deleteStudentById(Long id);

    /**
     * Deletes all students from the database.
     */
    void deleteAllStudents();
    
    /**
     * Deletes a student by their email.
     * 
     * @param email The email of the student to delete.
     */
    void deleteStudentByEmail(String email);

    /**
     * Deletes all students that match a given first name.
     * 
     * @param firstName The first name of students to delete.
     */
    void deleteStudentsByFirstName(String firstName);

    /**
     * Deletes all students that match a given last name.
     * 
     * @param lastName The last name of students to delete.
     */
    void deleteStudentsByLastName(String lastName);

    /**
     * Deletes all students from a given country.
     * 
     * @param country The country of students to delete.
     */
    void deleteStudentsByCountry(String country);

    /**
     * Deletes all students with a given nationality.
     * 
     * @param nationality The nationality of students to delete.
     */
    void deleteStudentsByNationality(String nationality);
    
    
    public List<Student> saveAll(List<Student> students);
}