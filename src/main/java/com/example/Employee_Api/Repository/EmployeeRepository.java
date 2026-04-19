package com.example.Employee_Api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Employee_Api.Model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
