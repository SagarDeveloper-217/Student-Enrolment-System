package studentInfo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import studentInfo.entities.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {

	// 9
	@Query("from Student s where s.batch.code=:code")
	List<Student> findByCode(String code);

	// 10
	List<Student> findByBatchId(Integer batch);

	// 12
	Page<Student> findByStudentNameContaining(String name, PageRequest pageRequest);

}
