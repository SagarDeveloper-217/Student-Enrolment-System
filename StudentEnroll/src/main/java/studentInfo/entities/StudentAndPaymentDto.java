package studentInfo.entities;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class StudentAndPaymentDto {


	private String studentName;

	private String email;

	private String mobile;
	private Integer batchId;

	private LocalDate date;

	private Double amount;

	private LocalDate payDate;

	private char payMode;

	public StudentAndPaymentDto(String studentName, String email, String mobile, Integer batchId, LocalDate date,
			Double amount, LocalDate payDate, char payMode) {
		super();
		this.studentName = studentName;
		this.email = email;
		this.mobile = mobile;
		this.batchId = batchId;
		this.date = date;
		this.amount = amount;
		this.payDate = payDate;
		this.payMode = payMode;
	}

	public StudentAndPaymentDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStudentName() {
		return studentName;
	}

	public String getEmail() {
		return email;
	}

	public String getMobile() {
		return mobile;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public LocalDate getDate() {
		return date;
	}

	public Double getAmount() {
		return amount;
	}

	public LocalDate getPayDate() {
		return payDate;
	}

	public char getPayMode() {
		return payMode;
	}

}
