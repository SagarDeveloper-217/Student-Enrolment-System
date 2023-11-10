package studentInfo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import studentInfo.entities.Batch;
import studentInfo.entities.Student;

public interface BatchRepo extends JpaRepository<Batch, Integer> {

//	// 6
//	@Query("from Batch b where :today between b.startDate and b.endDate")
//	List<Batch> getRunningBatch(@Param("today") LocalDate today);
//
//	// 7
//	@Query(value="SELECT * FROM Batch b where b.EndDate <:today ",nativeQuery=true)
//	List<Batch> getCompletedBatch(LocalDate today);
//
//	// 8
//	@Query("select b.students from Batch b where :today between b.startDate and b.endDate")
//	List<Student> getRunningBatchStudents(@Param("today") LocalDate today);
	
	//6
	@Query(value = "SELECT * FROM Batches b WHERE :today BETWEEN b.Startdate AND b.Enddate", nativeQuery = true)
	List<Batch> getRunningBatch(@Param("today") LocalDate today);


	//7
	@Query(value = "SELECT * FROM Batches b WHERE b.Enddate < :today", nativeQuery = true)
	List<Batch> getCompletedBatch(@Param("today") LocalDate today);

	//8
	@Query(value = "SELECT s.* FROM Batches b JOIN Students s ON b.id = s.batchid WHERE :today BETWEEN b.Startdate AND b.Enddate", nativeQuery = true)
	List<Student> getRunningBatchStudent(@Param("today") LocalDate today);

	// 11
	List<Batch> findByCode(String code);

	// 13
	@Query("from Batch b where  b.startDate between :firstdate and :seconddate ")
	List<Batch> getRunningBatchBetweenDates(@Param("firstdate") LocalDate firstdate,
			@Param("seconddate") LocalDate seconddate);

}