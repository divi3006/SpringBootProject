package com.example.wholesalehub.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {//user login requests
	
	@NotBlank(message = "{email.required}")
    @Email(message = "{email.invalid}")
	private String email;


	@NotBlank(message = "{password.required}")
	@Pattern(
		    regexp = "^[A-Z](?=.*[a-z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{7,}$",
		    message = "Password must start with an uppercase letter, contain at least one lowercase letter, one digit, one special character, and be at least 8 characters long"
		)
	@Schema(example = "Example@123") 
	private String password;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
