package ch.devprojects.sms.service.impl;

import ch.devprojects.sms.entity.Enrollment;
import ch.devprojects.sms.entity.Student;
import ch.devprojects.sms.entity.Course;
import ch.devprojects.sms.repository.EnrollmentRepository;
import ch.devprojects.sms.repository.StudentRepository;
import ch.devprojects.sms.repository.CourseRepository;
import ch.devprojects.sms.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Enrollment createEnrollment(Enrollment enrollment) {
        Long studentId = enrollment.getStudent().getId();
        Long courseId = enrollment.getCourse().getId();

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    @Override
    public Optional<Enrollment> updateEnrollment(Long id, Enrollment updatedEnrollment) {
        return enrollmentRepository.findById(id)
                .map(enrollment -> {
                    Long studentId = updatedEnrollment.getStudent().getId();
                    Long courseId = updatedEnrollment.getCourse().getId();

                    Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
                    Course course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

                    enrollment.setStudent(student);
                    enrollment.setCourse(course);
                    enrollment.setEnrollmentDate(updatedEnrollment.getEnrollmentDate());
                    enrollment.setStatus(updatedEnrollment.getStatus());
                    enrollment.setGrade(updatedEnrollment.getGrade());
                    enrollment.setNotes(updatedEnrollment.getNotes());

                    return enrollmentRepository.save(enrollment);
                });
    }

    @Override
    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }
}