package com.example.Basic.Entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Employee")
@Getter
@Setter
public class Employee {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private int empid;
	
	private String empname;
	
	private String depname;
	
	private double salary;
	
	private LocalDate joiningdate;
	
	
}
