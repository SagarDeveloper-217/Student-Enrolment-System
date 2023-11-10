package studentInfo.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Courses")
public class Course {
	@Id
	@Column(name = "Code")
	@NotBlank(message = "Code is required")
	@NotNull(message = "Code must not be null")
	@Size(max = 5, message = "Size of code should be mostbe 5 characters")
	private String code;

	@Column(name = "Name")
	@NotBlank(message = "Name must be required")
	@NotNull(message = "Name must not be null")
	private String name;

	@Column(name = "Durationindays")
	@NotNull(message = "Durationindays must be required")
	private int days;

	@Column(name = "Fee")
	@NotNull(message = "Fee must be required")
	@Positive(message = "Fee must be positive")
	private double fee;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
	@JsonIgnore
	private List<Batch> batches = new ArrayList<Batch>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public List<Batch> getBatches() {
		return batches;
	}

	@Override
	public String toString() {
		return "Course [code=" + code + ", name=" + name + ", days=" + days + ", fee=" + fee + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Course) {
			Course other = (Course) obj;
			return Objects.equals(code, other.code) && Objects.equals(name, other.name);
		} else
			return false;
	}

}
