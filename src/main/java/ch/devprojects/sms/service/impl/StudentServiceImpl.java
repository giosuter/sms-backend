package ch.devprojects.sms.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.devprojects.sms.entity.Student;
import ch.devprojects.sms.repository.StudentRepository;
import ch.devprojects.sms.service.StudentService;

/**
 * Implementation of the StudentService interface.
 * This class provides the actual logic for retrieving student data.
 */
@Service // Marks this class as a Spring service component
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    /**
     * Constructor-based dependency injection for StudentRepository.
     * 
     * @param studentRepository The repository handling student database operations.
     */
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves all students from the database.
     */
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Retrieves a student by their unique ID.
     */
    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Retrieves a student by their email.
     */
    @Override
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    /**
     * Retrieves a list of students by their first name.
     */
    @Override
    public List<Student> getStudentsByFirstName(String firstName) {
        return studentRepository.findByFirstName(firstName);
    }

    /**
     * Retrieves a list of students by their last name.
     */
    @Override
    public List<Student> getStudentsByLastName(String lastName) {
        return studentRepository.findByLastName(lastName);
    }

    /**
     * Retrieves a list of students by their country.
     */
    @Override
    public List<Student> getStudentsByCountry(String country) {
        return studentRepository.findByCountry(country);
    }
    
    @Override
    public List<Student> getStudentsByNationality(String nationality) {
        return studentRepository.findByNationality(nationality);
    }
    
    /**
     * Saves a new student to the database.
     * 
     * @param student The student entity to be persisted.
     * @return The saved student object.
     */
    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    
    /**
     * Saves multiple students in a batch operation.
     * @param students List of students to save.
     * @return List of saved students.
     */
    @Override
    @Transactional
    public List<Student> createStudents(List<Student> students) {
        return studentRepository.saveAll(students);
    }

    /**
     * Registers a student with minimal fields.
     * @param student The student object with only required fields.
     * @return The registered student.
     */
    @Override
    public Student registerStudent(Student student) {
        // Ensure required fields are present
        if (student.getFirstName() == null || student.getLastName() == null || student.getEmail() == null) {
            throw new IllegalArgumentException("First name, last name, and email are required.");
        }
        return studentRepository.save(student);
    }
    
    /**
     * Updates an existing student's details in the database.
     *
     * @param id The ID of the student to be updated.
     * @param updatedStudent The student object with new details.
     * @return The updated student entity, or empty if the student does not exist.
     */
    @Override
    public Optional<Student> updateStudent(Long id, Student updatedStudent) {
        // Check if the student exists
        return studentRepository.findById(id).map(existingStudent -> {
            // Update fields only if they are not null
            if (updatedStudent.getFirstName() != null) {
                existingStudent.setFirstName(updatedStudent.getFirstName());
            }
            if (updatedStudent.getLastName() != null) {
                existingStudent.setLastName(updatedStudent.getLastName());
            }
            if (updatedStudent.getAddress() != null) {
                existingStudent.setAddress(updatedStudent.getAddress());
            }
            if (updatedStudent.getPlz() != null) {
                existingStudent.setPlz(updatedStudent.getPlz());
            }
            if (updatedStudent.getCity() != null) {
                existingStudent.setCity(updatedStudent.getCity());
            }
            if (updatedStudent.getCountry() != null) {
                existingStudent.setCountry(updatedStudent.getCountry());
            }
            if (updatedStudent.getNationality() != null) {
                existingStudent.setNationality(updatedStudent.getNationality());
            }
            if (updatedStudent.getEmail() != null) {
                existingStudent.setEmail(updatedStudent.getEmail());
            }
            if (updatedStudent.getPhoneNumber() != null) {
                existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
            }
            if (updatedStudent.getDateOfBirth() != null) {
                existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
            }
            if (updatedStudent.getGender() != null) {
                existingStudent.setGender(updatedStudent.getGender());
            }
            if (updatedStudent.getStudentId() != null) {
                existingStudent.setStudentId(updatedStudent.getStudentId());
            }
            if (updatedStudent.getEnrollmentDate() != null) {
                existingStudent.setEnrollmentDate(updatedStudent.getEnrollmentDate());
            }
            if (updatedStudent.getStatus() != null) {
                existingStudent.setStatus(updatedStudent.getStatus());
            }
            if (updatedStudent.getProgramLevel() != null) {
                existingStudent.setProgramLevel(updatedStudent.getProgramLevel());
            }

            // Save and return updated student
            return studentRepository.save(existingStudent);
        });
    }
    

    /**
     * Updates specific fields of a student dynamically.
     *
     * @param id The ID of the student to update.
     * @param updates A map of field names and their new values.
     * @return An Optional containing the updated student, or empty if not found.
     */
    @Override
    @Transactional
    public Optional<Student> updateStudentFields(Long id, Map<String, Object> updates) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return Optional.empty();
        }

        Student student = optionalStudent.get();

        updates.forEach((key, value) -> {
            try {
                Field field = Student.class.getDeclaredField(key);
                field.setAccessible(true); // Allow modification of private fields
                field.set(student, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Invalid field: " + key, e);
            }
        });

        return Optional.of(studentRepository.save(student));
    }
    
    /**
     * Deletes a student by their ID if they exist.
     * 
     * @param id The ID of the student to be deleted.
     */
    @Override
    @Transactional
    public void deleteStudentById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Student with ID " + id + " not found.");
        }
    }

    /**
     * Deletes all students from the database.
     */
    @Override
    @Transactional
    public void deleteAllStudents() {
        studentRepository.deleteAll();
    }
    
    /**
     * Deletes a student by their email if they exist.
     * Throws an exception if no student is found with the given email.
     * 
     * @param email The email of the student to delete.
     */
    @Override
    @Transactional
    public void deleteStudentByEmail(String email) {
        if (studentRepository.existsByEmail(email)) {
            studentRepository.deleteByEmail(email);
        } else {
            throw new RuntimeException("Student with email " + email + " not found.");
        }
    }

    /**
     * Deletes all students with the given first name.
     * Throws an exception if no students match the given first name.
     * 
     * @param firstName The first name of students to delete.
     */
    @Override
    @Transactional
    public void deleteStudentsByFirstName(String firstName) {
        List<Student> students = studentRepository.findByFirstName(firstName);
        if (!students.isEmpty()) {
            studentRepository.deleteAll(students);
        } else {
            throw new RuntimeException("No students found with first name: " + firstName);
        }
    }

    /**
     * Deletes all students with the given last name.
     * Throws an exception if no students match the given last name.
     * 
     * @param lastName The last name of students to delete.
     */
    @Override
    @Transactional
    public void deleteStudentsByLastName(String lastName) {
        List<Student> students = studentRepository.findByLastName(lastName);
        if (!students.isEmpty()) {
            studentRepository.deleteAll(students);
        } else {
            throw new RuntimeException("No students found with last name: " + lastName);
        }
    }

    /**
     * Deletes all students from a given country.
     * Throws an exception if no students are found from that country.
     * 
     * @param country The country of students to delete.
     */
    @Override
    @Transactional
    public void deleteStudentsByCountry(String country) {
        List<Student> students = studentRepository.findByCountry(country);
        if (!students.isEmpty()) {
            studentRepository.deleteAll(students);
        } else {
            throw new RuntimeException("No students found from country: " + country);
        }
    }

    /**
     * Deletes all students with a given nationality.
     * Throws an exception if no students match the given nationality.
     * 
     * @param nationality The nationality of students to delete.
     */
    @Override
    @Transactional
    public void deleteStudentsByNationality(String nationality) {
        List<Student> students = studentRepository.findByNationality(nationality);
        if (!students.isEmpty()) {
            studentRepository.deleteAll(students);
        } else {
            throw new RuntimeException("No students found with nationality: " + nationality);
        }
    }
    
    public List<Student> saveAll(List<Student> students) {
        return studentRepository.saveAll(students);
    }
}