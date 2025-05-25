package ch.devprojects.sms.service;

import ch.devprojects.sms.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Course createCourse(Course course);

    List<Course> getAllCourses();

    Optional<Course> getCourseById(Long id);

    Optional<Course> updateCourse(Long id, Course updatedCourse);

    void deleteCourse(Long id);
}