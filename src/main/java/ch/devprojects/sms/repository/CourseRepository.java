package ch.devprojects.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ch.devprojects.sms.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}