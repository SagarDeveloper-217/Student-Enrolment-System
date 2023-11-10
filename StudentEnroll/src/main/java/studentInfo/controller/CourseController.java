package studentInfo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import studentInfo.entities.Batch;
import studentInfo.entities.Course;
import studentInfo.entities.Payment;
import studentInfo.entities.Student;
import studentInfo.entities.StudentAndPaymentDto;
import studentInfo.repositories.BatchRepo;
import studentInfo.repositories.CourseRepo;
import studentInfo.repositories.PaymentRepo;
import studentInfo.repositories.StudentRepo;

@RestController
public class CourseController {

	@Autowired
	CourseRepo courseRepo;

//-------------------------------------------------------------------------
	// Add Course
	// 1
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addcourses")
	@Operation(summary = "Add Courses", description = "Adding a course")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Added"),
			@ApiResponse(responseCode = "400", description = "Bad request course or code is already present"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Course addNewcourse(@Valid @RequestBody Course c) {
		try {
			List<Course> courses = courseRepo.findAll();
			boolean check = courses.contains(c);
			if (check) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Code Already Present");
			}
			courseRepo.save(c);
			return c;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatecoursebycode/{code}")
	@Operation(summary = "update Course of given Code", description = "Update the row of Course of given Code")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Updated"),
			@ApiResponse(responseCode = "404", description = "course code NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public void updateOneCourse(@Valid @PathVariable("code") String code, @RequestBody Course course) {
		try {
			var get = courseRepo.findById(code);
			if (get.isPresent()) {
				Course c = get.get();
				c.setCode(course.getCode());
				c.setDays(course.getDays());
				c.setFee(course.getFee());
				c.setName(course.getName());
				courseRepo.save(c);

			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Name Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deletebycoursecode/{code}")
	@Operation(summary = "Delete course", description = "Delets the course of given code")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Deleted"),
			@ApiResponse(responseCode = "404", description = " course code NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public void deleteOneCourse(
			@Parameter(description = "Course that is to be deleted", allowEmptyValue = false) @PathVariable("code") String code) {
		try {
			var course = courseRepo.findById(code);
			if (course.isPresent())
				courseRepo.deleteById(code);
			else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Name Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}

	}

	// 5
	// List All Courses
	@CrossOrigin
	@GetMapping("/courses")
	@Operation(summary = "Get All Courses", description = "Retrives all the rows of Courses")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Course> course() {
		return courseRepo.findAll();
	}

}
