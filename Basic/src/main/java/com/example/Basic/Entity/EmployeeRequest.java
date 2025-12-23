package com.example.Basic.Entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequest {
	
	@NotEmpty(message="empname Required")
	private String empname;
	
	@NotEmpty(message="depname Required")
	private String depname;
	
	@Min(value=10000,message="minimum salary  must be 10000")
	private double salary;

}
