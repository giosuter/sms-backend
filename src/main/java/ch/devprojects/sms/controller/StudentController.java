package ch.devprojects.sms.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.devprojects.sms.entity.Student;
import ch.devprojects.sms.service.StudentService;

/**
 * REST Controller for managing student data.
 * Provides endpoints to retrieve students based on different search criteria.
 *
 * Base path: /students (since context-path is already /sms)
 */
//@CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
@RestController
@RequestMapping({"/students", "/students/"}) // Endpoints will be under /sms/students and /sms/students/
public class StudentController {

    private final StudentService studentService;

    /**
     * Constructor-based dependency injection of StudentService.
     * @param studentService The service handling business logic for Student entity.
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Retrieves all students.
     * GET /sms/students
     *
     * @return List of all students.
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Retrieves a student by ID.
     * GET /sms/students/{id}
     *
     * @param id The unique ID of the student.
     * @return Student if found, otherwise 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a student by email.
     * GET /sms/students/email/{email}
     *
     * @param email The email of the student.
     * @return Student if found, otherwise 404 Not Found.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email) {
        Optional<Student> student = studentService.getStudentByEmail(email);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves students by first name.
     * GET /sms/students/firstname/{firstName}
     *
     * @param firstName The first name to filter students by.
     * @return List of students with the specified first name.
     */
    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<List<Student>> getStudentsByFirstName(@PathVariable String firstName) {
        List<Student> students = studentService.getStudentsByFirstName(firstName);
        return ResponseEntity.ok(students);
    }

    /**
     * Retrieves students by last name.
     * GET /sms/students/lastname/{lastName}
     *
     * @param lastName The last name to filter students by.
     * @return List of students with the specified last name.
     */
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<Student>> getStudentsByLastName(@PathVariable String lastName) {
        List<Student> students = studentService.getStudentsByLastName(lastName);
        return ResponseEntity.ok(students);
    }

    /**
     * Retrieves students by country.
     * GET /sms/students/country/{country}
     *
     * @param country The country to filter students by.
     * @return List of students from the specified country.
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<Student>> getStudentsByCountry(@PathVariable String country) {
        List<Student> students = studentService.getStudentsByCountry(country);
        return ResponseEntity.ok(students);
    }

    /**
     * Retrieves students by nationality.
     * GET /sms/students/nationality/{nationality}
     *
     * @param nationality The nationality to filter students by.
     * @return List of students with the specified nationality.
     */
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<Student>> getStudentsByNationality(@PathVariable String nationality) {
        List<Student> students = studentService.getStudentsByNationality(nationality);
        return ResponseEntity.ok(students);
    }
    
    /**
     * Handles HTTP POST request to create a new student. 
     * Endpoint for creating a new student
     * 
     * @param student The student data from the request body.
     * @return ResponseEntity with the created student and HTTP 200 status.
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student savedStudent = studentService.createStudent(student);
        return ResponseEntity.ok(savedStudent);
    }
    
    /**
     * Endpoint to create multiple students in bulk.
     * @param students List of students to be saved.
     * @return ResponseEntity containing the list of saved students.
     */
    /*
     * @PostMapping("/bulk")
     
    public ResponseEntity<List<Student>> createStudents(@RequestBody List<Student> students) {
        List<Student> createdStudents = studentService.createStudents(students);
        return ResponseEntity.ok(createdStudents);
    }*/
    
    @PostMapping("/bulk")
    public ResponseEntity<List<Student>> bulkCreateStudents(@RequestBody List<Student> students) {
        List<Student> savedStudents = studentService.saveAll(students);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudents);
    }

    /**
     * Endpoint to create a student with only required fields.
     * @param student The student object with minimal fields.
     * @return ResponseEntity containing the created student.
     */
    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        Student registeredStudent = studentService.registerStudent(student);
        return ResponseEntity.ok(registeredStudent);
    }
    
    /**
     * Handles updating an existing student.
     *
     * @param id The ID of the student to be updated.
     * @param updatedStudent The new student data.
     * @return ResponseEntity with the updated student or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        Optional<Student> updated = studentService.updateStudent(id, updatedStudent);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * Handles HTTP PATCH request for partial updates.
     *
     * @param id The ID of the student to update.
     * @param updates A map containing the fields to be updated and their new values.
     * @return ResponseEntity with the updated student or 404 if not found.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Student> updateStudentFields(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Student> updatedStudent = studentService.updateStudentFields(id, updates);
        return updatedStudent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * Deletes a student by ID.
     * DELETE /sms/students/{id}
     *
     * @param id The unique ID of the student to delete.
     * @return ResponseEntity with 204 No Content if successful, 404 Not Found if student does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Long id) {
        try {
            studentService.deleteStudentById(id);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content on success
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // HTTP 404 Not Found if student not found
        }
    }

    /**
     * Deletes all students.
     * DELETE /sms/students
     *
     * @return ResponseEntity with 204 No Content if successful.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllStudents() {
        studentService.deleteAllStudents();
        return ResponseEntity.noContent().build(); // HTTP 204 No Content on success
    }
    
    /**
     * Deletes a student by email.
     * DELETE /sms/students/email/{email}
     *
     * @param email The email of the student to delete.
     * @return HTTP 204 No Content if successful, or 404 Not Found if student is not found.
     */
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteStudentByEmail(@PathVariable String email) {
        try {
            studentService.deleteStudentByEmail(email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes students by first name.
     * DELETE /sms/students/firstname/{firstName}
     *
     * @param firstName The first name of students to delete.
     * @return HTTP 204 No Content if successful, or 404 Not Found if no students are found.
     */
    @DeleteMapping("/firstname/{firstName}")
    public ResponseEntity<Void> deleteStudentsByFirstName(@PathVariable String firstName) {
        try {
            studentService.deleteStudentsByFirstName(firstName);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes students by last name.
     * DELETE /sms/students/lastname/{lastName}
     *
     * @param lastName The last name of students to delete.
     * @return HTTP 204 No Content if successful, or 404 Not Found if no students are found.
     */
    @DeleteMapping("/lastname/{lastName}")
    public ResponseEntity<Void> deleteStudentsByLastName(@PathVariable String lastName) {
        try {
            studentService.deleteStudentsByLastName(lastName);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes students by country.
     * DELETE /sms/students/country/{country}
     *
     * @param country The country of students to delete.
     * @return HTTP 204 No Content if successful, or 404 Not Found if no students are found.
     */
    @DeleteMapping("/country/{country}")
    public ResponseEntity<Void> deleteStudentsByCountry(@PathVariable String country) {
        try {
            studentService.deleteStudentsByCountry(country);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes students by nationality.
     * DELETE /sms/students/nationality/{nationality}
     *
     * @param nationality The nationality of students to delete.
     * @return HTTP 204 No Content if successful, or 404 Not Found if no students are found.
     */
    @DeleteMapping("/nationality/{nationality}")
    public ResponseEntity<Void> deleteStudentsByNationality(@PathVariable String nationality) {
        try {
            studentService.deleteStudentsByNationality(nationality);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}