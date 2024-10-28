package com.ensa.devsecops_exam_moad.repository;

import com.ensa.devsecops_exam_moad.module.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);
}
