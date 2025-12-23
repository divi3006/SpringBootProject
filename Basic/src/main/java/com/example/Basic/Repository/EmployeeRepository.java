package com.example.Basic.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Basic.Entity.Employee;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee,Integer>{
	
	boolean existsByEmpname(String empname);

}
