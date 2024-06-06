package com.Employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Employees.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
