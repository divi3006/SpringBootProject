package com.example.Basic.Exception;

public class EmployeeAlreadyExistsException extends RuntimeException {
	
	public EmployeeAlreadyExistsException(String message) {
	super(message);
	}

}
