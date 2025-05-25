package ch.devprojects.sms.service;

import ch.devprojects.sms.entity.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
	List<Enrollment> getAllEnrollments();

	Optional<Enrollment> getEnrollmentById(Long id);

	Enrollment createEnrollment(Enrollment enrollment);

	Optional<Enrollment> updateEnrollment(Long id, Enrollment enrollment);

	void deleteEnrollment(Long id);
}