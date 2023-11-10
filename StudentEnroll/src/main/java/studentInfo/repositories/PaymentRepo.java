package studentInfo.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import studentInfo.entities.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

	// 14
	@Query("from Payment p where p.student.batchId=:id")
	List<Payment> getByBatch(Integer id);

	// 15
	@Query("from Payment p where  p.payDate between :startdate and :enddate ")
	List<Payment> getBatchBetween(LocalDate startdate, LocalDate enddate);

	// 16
	List<Payment> findByPayMode(char mode);

	// 3(update)
	Optional<Payment> findByStudentId(Integer Id);

}
