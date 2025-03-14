package ch.devprojects.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ch.devprojects.sms.entity.Student;

/**
 * Repository interface for Student entity. This interface provides 
 * basic CRUD operations and custom queries for Student objects.
 * 
 * Repository Layer (Data Access Layer)
 * ------------------------------------
 * Purpose:
 * -------
 * The repository layer is responsible for <b>direct</b> interaction with the database. 
 * It acts as a bridge between the application and the database, handling data 
 * retrieval, storage, and manipulation.
 */
@Repository // Marks this interface as a Spring Data repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findByEmail(String email);
	Optional<Student> findByPhoneNumber(String phoneNumber);
	Optional<Student> findByStudentId(String studentId);
	List<Student> findByFirstName(String firstName);
	List<Student> findByLastName(String lastName);
	List<Student> findByCity(String city);
	List<Student> findByCountry(String country);
	List<Student> findByNationality(String nationality);
	
	List<Student> findByProgramLevel(String programLevel);
	List<Student> findByStatus(String status);
    /**
     * Checks if a student exists with the given email.
     * 
     * @param email The email to search for.
     * @return True if a student exists with this email, otherwise false.
     */
    boolean existsByEmail(String email);

    /**
     * Deletes a student by email.
     * 
     * @param email The email of the student to delete.
     */
    @Transactional
    void deleteByEmail(String email);
}
