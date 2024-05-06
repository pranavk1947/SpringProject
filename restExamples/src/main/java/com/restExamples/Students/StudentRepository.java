package com.restExamples.Students;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>{
	Student findByRollNumber(String rollNumber);

}
