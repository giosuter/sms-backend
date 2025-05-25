package ch.devprojects.sms.service.impl;

import ch.devprojects.sms.entity.Course;
import ch.devprojects.sms.repository.CourseRepository;
import ch.devprojects.sms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> updateCourse(Long id, Course updatedCourse) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setCourseName(updatedCourse.getCourseName());
                    course.setDescription(updatedCourse.getDescription());
                    course.setDuration(updatedCourse.getDuration());
                    course.setStartDate(updatedCourse.getStartDate());
                    course.setEndDate(updatedCourse.getEndDate());
                    course.setProfessorId(updatedCourse.getProfessorId());
                    return courseRepository.save(course);
                });
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}