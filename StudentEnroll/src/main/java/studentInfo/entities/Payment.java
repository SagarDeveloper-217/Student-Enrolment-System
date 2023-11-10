package studentInfo.entities;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int paymentId;

	@Column(name = "Studentid")
	private int studentId;

	@Column(name = "Amount")
	@NotNull(message = "Amount cannot be null")
	@Positive(message = "Amount must be positive")
	private double amount;

	@Column(name = "Paydate")
	@NotNull(message = "Paydate must be required")
	private LocalDate payDate;

	@Column(name = "Paymode")
	@NotNull(message = "PayMode must be given")
	private char payMode;

	@OneToOne(mappedBy = "payment")
	@JsonIgnore
	private Student student;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getPayDate() {
		return payDate;
	}

	public void setPayDate(LocalDate payDate) {
		this.payDate = payDate;
	}

	public char getPayMode() {
		return payMode;
	}

	public void setPayMode(char payMode) {
		this.payMode = payMode;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", studentId=" + studentId + ", amount=" + amount + ", payDate="
				+ payDate + ", payMode=" + payMode + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(paymentId, studentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Payment) {
			Payment other = (Payment) obj;
			return paymentId == other.paymentId && studentId == other.studentId;
		} else
			return false;
	}

}
