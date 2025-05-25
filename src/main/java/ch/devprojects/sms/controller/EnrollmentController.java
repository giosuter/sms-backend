package ch.devprojects.sms.controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.devprojects.sms.dto.EnrollmentDTO;
import ch.devprojects.sms.entity.Course;
import ch.devprojects.sms.entity.Enrollment;
import ch.devprojects.sms.entity.Student;
import ch.devprojects.sms.repository.CourseRepository;
import ch.devprojects.sms.repository.StudentRepository;
import ch.devprojects.sms.service.EnrollmentService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({"/enrollments", "/enrollments/"})
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final MessageSource messageSource;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentController(
            EnrollmentService enrollmentService,
            MessageSource messageSource,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.enrollmentService = enrollmentService;
        this.messageSource = messageSource;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public ResponseEntity<String> createEnrollment(@RequestBody EnrollmentDTO dto) {
        if (dto.getStudentId() == null || dto.getCourseId() == null) {
            String error = messageSource.getMessage("error.invalidInput", null, LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest().body(error);
        }

        Optional<Student> studentOpt = studentRepository.findById(dto.getStudentId());
        Optional<Course> courseOpt = courseRepository.findById(dto.getCourseId());

        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            String error = messageSource.getMessage("error.notfound", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(404).body(error);
        }

        Enrollment enrollment = new Enrollment(
                studentOpt.get(),
                courseOpt.get(),
                dto.getEnrollmentDate(),
                dto.getStatus(),
                dto.getGrade(),
                dto.getNotes()
        );

        enrollmentService.createEnrollment(enrollment);
        String message = messageSource.getMessage("enrollment.created", null, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        List<EnrollmentDTO> dtoList = enrollmentService.getAllEnrollments().stream()
            .map(e -> new EnrollmentDTO(
                e.getId(),
                e.getStudent().getId(),
                e.getStudent().getFirstName() + " " + e.getStudent().getLastName(),
                e.getCourse().getId(),
                e.getCourse().getName(),
                e.getEnrollmentDate(),
                e.getStatus(),
                e.getGrade(),
                e.getNotes()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(id);
        return enrollment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEnrollment(@PathVariable Long id,
                                                   @RequestBody EnrollmentDTO dto,
                                                   @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        Optional<Student> studentOpt = studentRepository.findById(dto.getStudentId());
        Optional<Course> courseOpt = courseRepository.findById(dto.getCourseId());

        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            String error = messageSource.getMessage("error.notfound", null, locale);
            return ResponseEntity.status(404).body(error);
        }

        Enrollment updatedEnrollment = new Enrollment(
                studentOpt.get(),
                courseOpt.get(),
                dto.getEnrollmentDate(),
                dto.getStatus(),
                dto.getGrade(),
                dto.getNotes()
        );
        updatedEnrollment.setId(id);

        Optional<Enrollment> result = enrollmentService.updateEnrollment(id, updatedEnrollment);
        if (result.isPresent()) {
            String message = messageSource.getMessage("enrollment.updated", null, locale);
            return ResponseEntity.ok(message);
        } else {
            String error = messageSource.getMessage("error.notfound", null, locale);
            return ResponseEntity.status(404).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEnrollment(@PathVariable Long id,
                                                   @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        enrollmentService.deleteEnrollment(id);
        String message = messageSource.getMessage("enrollment.deleted", null, locale);
        return ResponseEntity.ok(message);
    }
}