package studentInfo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import studentInfo.entities.Course;

public interface CourseRepo extends JpaRepository<Course, String> {

}
