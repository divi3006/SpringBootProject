package com.example.Basic.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Basic.Entity.Employee;
import com.example.Basic.Entity.EmployeeRequest;
import com.example.Basic.Service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/EmployeeDetails")
public class EmployeeController {
	
	@Autowired
	private EmployeeService empService;
	
	@PostMapping("/AddEmp")
	public ResponseEntity<String>addemp(@Valid @RequestBody EmployeeRequest empReq){
		empService.addemp(empReq);
		return ResponseEntity.ok("Employee added successfully");
		
	}
	
	@GetMapping("/GetAllEmp")
	
	public List<Employee>getAllEmp(){
		return empService.getAllEmp();
	}
	
	
	@GetMapping("/GetEmpbyId")
	public ResponseEntity<Employee>getEmpbyId(@RequestParam int empid){
		Employee emp=empService.getempbyid(empid);
		return ResponseEntity.ok(emp);
	}
	
	@PutMapping("/UpdateEmp")
	public ResponseEntity<String>updateEmp(@RequestParam int empid,@Valid @RequestBody EmployeeRequest empReq){
		empService.updateEmp(empid,empReq);
		return ResponseEntity.ok("Employee updated successfully");
	}

	
	@DeleteMapping("/DeletempbyId")
	public ResponseEntity<String>deleteEmpbyId(@RequestParam int empid){
		Employee emps=empService.deleteempbyid(empid);
		return ResponseEntity.ok("Employee deleted successfully");
	}
	
}
