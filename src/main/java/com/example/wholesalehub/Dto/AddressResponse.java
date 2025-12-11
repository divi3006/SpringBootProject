package com.example.wholesalehub.Dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddressResponse {
	
	 @NotBlank(message = "Street is required")
	   private String street;
		    
		 @NotBlank(message = "City is required")
	    private String city;

		    @NotBlank(message = "State is required")
		 private String state;
		    
		    @NotNull(message = "Pin code is required")
	      	private int pincode;
	

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}


}
