package ch.devprojects.sms.dto;

import java.time.LocalDate;

public class EnrollmentDTO {
	private Long id;
	private Long studentId;
	private String studentName;
	private Long courseId;
	private String courseName;
	private LocalDate enrollmentDate;
	private String status;
	private String grade;
	private String notes;

	public EnrollmentDTO() {
	}

	public EnrollmentDTO(Long id, Long studentId, String studentName, Long courseId, String courseName,
			LocalDate enrollmentDate, String status, String grade, String notes) {
		this.id = id;
		this.studentId = studentId;
		this.studentName = studentName;
		this.courseId = courseId;
		this.courseName = courseName;
		this.enrollmentDate = enrollmentDate;
		this.status = status;
		this.grade = grade;
		this.notes = notes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public LocalDate getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(LocalDate enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}