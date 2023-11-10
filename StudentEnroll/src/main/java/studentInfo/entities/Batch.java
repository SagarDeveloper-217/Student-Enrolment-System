package studentInfo.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Batches")

public class Batch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int batchId;

	@Column(name = "Coursecode")
	@Size(max = 5, message = "Course code length must be less than 5")
	private String code;

	@Column(name = "Startdate")
	@NotNull(message = "StartDate must be required")
	private LocalDate startDate;

	@Transient
	private LocalDate endDate;

	@Column(name = "Timings")
	@NotNull(message = "Timings must not be null")
	@NotBlank(message = "Timings must be required")
	private String time;

	@Column(name = "Durationindays")
	@NotNull(message = "days must not be null")
	@Positive(message = "Durationindays must be positive")
	private int duration;

	@Column(name = "Fee")
	@NotNull(message = "fee must not be null")
	@Positive(message = "Fee must be positive")
	private double fee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Coursecode", insertable = false, updatable = false)
	@JsonIgnore
	private Course course;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "batch")
	@JsonIgnore
	private List<Student> students = new ArrayList<Student>();

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return getStartDate().plusDays(getDuration());
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return "Batch [batchId=" + batchId + ", code=" + code + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", time=" + time + ", duration=" + duration + ", fee=" + fee + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(batchId, startDate, time);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Batch) {
			Batch other = (Batch) obj;
			return batchId == other.batchId && Objects.equals(startDate, other.startDate)
					&& Objects.equals(time, other.time);
		} else
			return false;
	}

}
