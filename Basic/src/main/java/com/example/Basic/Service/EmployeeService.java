package com.example.Basic.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Basic.Entity.Employee;
import com.example.Basic.Entity.EmployeeRequest;
import com.example.Basic.Exception.EmployeeAlreadyExistsException;
import com.example.Basic.Exception.EmployeeNotFoundException;
import com.example.Basic.Repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository empRepo;

	public void addemp(EmployeeRequest empReq) {
		if(empRepo.existsByEmpname(empReq.getEmpname())) {
			throw new EmployeeAlreadyExistsException("Employee alerady exists with empname:"+empReq.getEmpname());
		}
			
			Employee em=new Employee();
			em.setEmpname(empReq.getEmpname());
			em.setDepname(empReq.getDepname());
			em.setSalary(empReq.getSalary());
			em.setJoiningdate(LocalDate.now());
			
		  empRepo.save(em);
		}

	public List<Employee> getAllEmp() {
		// TODO Auto-generated method stub
		return empRepo.findAll();
	}

	public Employee getempbyid(int empid) {
		
	return empRepo.findById(empid)
				.orElseThrow(()->new EmployeeNotFoundException("Employee not found with empid:"+empid));
		
	}

	public void updateEmp(int empid, EmployeeRequest empReq) {
		Employee ee=empRepo.findById(empid)
				.orElseThrow(()->new EmployeeNotFoundException("Employee not found with empid:"+empid));
	
		if(empRepo.existsByEmpname(empReq.getEmpname())) {
			throw new EmployeeAlreadyExistsException("Employee alerady exists with empname:"+empReq.getEmpname());
		}
		ee.setEmpname(empReq.getEmpname());
		ee.setDepname(empReq.getDepname());
		ee.setSalary(empReq.getSalary());
		 empRepo.save(ee);
	}

	public Employee deleteempbyid(int empid) {
		Employee emps=empRepo.findById(empid)
				.orElseThrow(()->new EmployeeNotFoundException("Employee not found with empid:"+empid));
		
		empRepo.deleteById(empid);
		return emps;
	
		
	}
	
		
		
	}

