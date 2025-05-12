package ch.devprojects.sms.service;

import ch.devprojects.sms.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCourses();
    Optional<Course> getCourseById(Long id);
    Course createCourse(Course course);
    Optional<Course> updateCourse(Long id, Course updatedCourse);
    void deleteCourse(Long id);
}