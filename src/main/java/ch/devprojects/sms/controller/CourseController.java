package ch.devprojects.sms.controller;

import ch.devprojects.sms.entity.Course;
import ch.devprojects.sms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({"/courses", "/courses/"})
public class CourseController {

    private final CourseService courseService;
    private final MessageSource messageSource;

    @Autowired
    public CourseController(CourseService courseService, MessageSource messageSource) {
        this.courseService = courseService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createCourse(@RequestBody Course course,
                                               @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        courseService.createCourse(course);
        String message = messageSource.getMessage("course.created", null, locale);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Long id,
                                               @RequestBody Course updatedCourse,
                                               @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        Optional<Course> updated = courseService.updateCourse(id, updatedCourse);
        if (updated.isPresent()) {
            String message = messageSource.getMessage("course.updated", null, locale);
            return ResponseEntity.ok(message);
        } else {
            String error = messageSource.getMessage("error.notfound", null, locale);
            return ResponseEntity.status(404).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id,
                                               @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        courseService.deleteCourse(id);
        String message = messageSource.getMessage("course.deleted", null, locale);
        return ResponseEntity.ok(message);
    }
}