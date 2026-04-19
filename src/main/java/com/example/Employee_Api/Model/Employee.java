package com.example.Employee_Api.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Role is required")
    @Column(nullable = false)
    private String role;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    @Column(nullable = false)
    private Double salary;

    // Constructors
    public Employee() {}

    public Employee(String name, String role, Double salary) {
        this.name = name;
        this.role = role;
        this.salary = salary;
    }


}
