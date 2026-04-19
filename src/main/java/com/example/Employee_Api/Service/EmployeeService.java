package com.example.Employee_Api.Service;
import java.util.List;

import com.example.Employee_Api.Model.Employee;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);
}
