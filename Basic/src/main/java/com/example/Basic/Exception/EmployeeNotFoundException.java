package com.example.Basic.Exception;

public class EmployeeNotFoundException extends RuntimeException {
	
	public EmployeeNotFoundException(String message) {
		super(message);
	}

}
