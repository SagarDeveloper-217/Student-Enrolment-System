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
import studentInfo.entities.Payment;
import studentInfo.entities.Student;
import studentInfo.entities.StudentAndPaymentDto;
import studentInfo.repositories.PaymentRepo;
import studentInfo.repositories.StudentRepo;

@RestController
public class StudentAndPaymentController {

	@Autowired
	StudentRepo studentRepo;

	@Autowired
	PaymentRepo paymentRepo;

	// List All Students
	@GetMapping("/student")
	@Operation(summary = "Get All Students", description = "Retrives List of Students")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Student> student() {
		return studentRepo.findAll();
	}

	// List All payments
	@GetMapping("/payment")
	@Operation(summary = "Get All Payments", description = "Retrives List of Payments")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Payment> payment() {
		return paymentRepo.findAll();
	}

	// 3
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addstudent")
	@Operation(summary = "Add Student", description = "Adding new Student")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Added"),
			@ApiResponse(responseCode = "400", description = "Bad request student incorrect details"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	@Transactional
	public StudentAndPaymentDto addNewStudentPayment(@Valid @RequestBody StudentAndPaymentDto newstudentandpayment) {
		try {
			Student s = new Student();
			s.setStudentName(newstudentandpayment.getStudentName());
			s.setEmail(newstudentandpayment.getEmail());
			s.setMobile(newstudentandpayment.getMobile());
			s.setBatchId(newstudentandpayment.getBatchId());
			s.setDate(newstudentandpayment.getDate());
			List<Student> existingstudent = studentRepo.findAll();
			boolean exist = existingstudent.contains(s);
			if (exist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student Already Present");
			studentRepo.save(s);
			Payment p = new Payment();
			p.setStudentId(s.getStudentId());
			p.setAmount(newstudentandpayment.getAmount());
			p.setPayDate(newstudentandpayment.getPayDate());
			p.setPayMode(newstudentandpayment.getPayMode());
			paymentRepo.save(p);
			return newstudentandpayment;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deletestudentbyid/{studentId}")
	@Operation(summary = "Delete Student", description = "Delete Student of particular Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Deleted"),
			@ApiResponse(responseCode = "404", description = "Student Id NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	@Transactional
	public void deleteOneStudent(
			@Parameter(description = "Student and payment that is to be deleted", allowEmptyValue = false) @PathVariable("studentId") Integer studentId) {
		try {
			Optional<Student> optStudent = studentRepo.findById(studentId);
			if (optStudent.isPresent()) {
				studentRepo.deleteById(studentId);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student Id Not Found!");
			}

		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatestudentbyid/{id}")
	@Operation(summary = "Update Student", description = "Update Students of Given Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Updated"),
			@ApiResponse(responseCode = "404", description = "student Id NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	@Transactional
	public StudentAndPaymentDto updateStudentPayment(@Valid @PathVariable("id") Integer id,
			@RequestBody StudentAndPaymentDto studentandpayment) {
		try {
			Optional<Student> exist = studentRepo.findById(id);
			if (!exist.isPresent())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with this id is Not Found!");
			Student s = exist.get();
			s.setStudentName(studentandpayment.getStudentName());
			s.setEmail(studentandpayment.getEmail());
			s.setMobile(studentandpayment.getMobile());
			s.setBatchId(studentandpayment.getBatchId());
			s.setDate(studentandpayment.getDate());
			studentRepo.save(s);
			Optional<Payment> payment = paymentRepo.findByStudentId(id);
			Payment p = payment.get();
			p.setAmount(studentandpayment.getAmount());
			p.setPayDate(studentandpayment.getPayDate());
			p.setPayMode(studentandpayment.getPayMode());
			paymentRepo.save(p);
			return studentandpayment;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 4
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatepaymentbyid/{id}")
	@Operation(summary = "Update Payment", description = "Update Payment of given Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Updated"),
			@ApiResponse(responseCode = "404", description = "Payment Id NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public void updateOnePayment(@Valid @PathVariable("id") Integer id, @RequestBody Payment payment) {
		try {
			var optpayment = paymentRepo.findById(id);
			if (optpayment.isPresent()) {
				Payment p = optpayment.get();
				p.setStudentId(payment.getStudentId());
				p.setAmount(payment.getAmount());
				p.setPayDate(payment.getPayDate());
				p.setPayMode(payment.getPayMode());
				paymentRepo.save(p);
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}

	}

	// 9
	@GetMapping("/getstudentbycode/{code}")
	@Operation(summary = "Get All students of  their course", description = "Retrives all the students of particular course")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public Object getStudentByCode(@PathVariable("code") String code) {
		try {
			return getStudentByCode1(code);
		} catch (ResponseStatusException ex) {
			return ex.getMessage();
		} catch (Exception ex) {
			return ex.getMessage();
		}
	}

	public List<Student> getStudentByCode1(String code) {
		List<Student> s = studentRepo.findByCode(code);
		if (s.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Code Not Found!");
		return s;
	}

	// 10
	@GetMapping("/getstudentsbybatch/{batch}")
	@Operation(summary = "Get Students of their Batch", description = "Retrives all the students of their batches giving batchId")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public List<Student> getStudentByBatch(@PathVariable("batch") Integer batch) {
		try {
			List<Student> s = studentRepo.findByBatchId(batch);
			if (!s.isEmpty())
				return s;
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Students Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 12
	@GetMapping("/getstudentsbypartialname/{name}")
	@Operation(summary = "Get Students based on given partial name", description = "Retrives all the Students based on their given partial name")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public Page<Student> getStudentByPartialName(@PathVariable("name") String name,
			@RequestParam(name = "pageSize", defaultValue = "2") int pageSize, Pageable pageable) {
		Sort sort = Sort.by("studentName").ascending(); // Sort by batch ID in ascending order
		try {
			Page<Student> s = studentRepo.findByStudentNameContaining(name,
					PageRequest.of(pageable.getPageNumber(), pageSize, sort));
			if (!s.isEmpty())
				return s;
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "students Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 14
	@GetMapping("/getpaymentsbybatchid/{id}")
	@Operation(summary = "Get Payments of batches", description = "Retrives all the payments of particular batch")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public List<Payment> getPaymentsByBatch(@PathVariable("id") Integer id) {
		try {
			List<Payment> p = paymentRepo.getByBatch(id);
			if (!p.isEmpty())
				return p;
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payments Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

	// 15
	@GetMapping("/paymentsbetween2dates")
	@Operation(summary = "Get payments btw two dates", description = "Retrives all the payments that are done between given two given dates")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public List<Payment> getPaymentsbetweentwodates(@RequestParam("startdate") LocalDate startdate,
			@RequestParam("enddate") LocalDate enddate) {
		try {
			List<Payment> p = paymentRepo.getBatchBetween(startdate, enddate);
			if (!p.isEmpty())
				return p;
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payments Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}

	}

	// 16
	@GetMapping("/getpaymentsbymode/{mode}")
	@Operation(summary = "Get payments by mode", description = "Retrives all the payments of particular payment mode ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully Retrived"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })

	public List<Payment> getPaymentsByBatch(@PathVariable("mode") char mode) {
		try {
			List<Payment> p = paymentRepo.findByPayMode(mode);
			if (!p.isEmpty())
				return p;
			else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment mode Not Found!");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (Exception ex) {
			throw ex;
		}
	}

}
