package com.example.Basic.Exception;

import java.util.HashMap;
import java.util.Map;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EmployeeAlreadyExistsException.class)
	public ResponseEntity<String> handleEmployeeExists(EmployeeAlreadyExistsException ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
	}
	
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String>handleNotFound(EmployeeNotFoundException ee){
    	return new ResponseEntity<>(ee.getMessage(),HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)	
	public ResponseEntity<Map<String,String>>handleValidation(MethodArgumentNotValidException ex){
		
		Map<String,String>errors=new HashMap<>();
		ex.getBindingResult().getFieldErrors()
		.forEach(error->errors.put(error.getField(),error.getDefaultMessage()));
		
		return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String>handlegenric(Exception e){
		return new ResponseEntity<>("Something went wrong"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
