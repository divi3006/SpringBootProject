package com.example.wholesalehub.Dto;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignupRequest {//taking user input during signup
	
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
	
	private Set<String> roles;
//Instead of storing role objects, it takes role names from the signup request.
	
//	
//	private String adminId;
//
//	
//	public String getAdminId() {
//		return adminId;
//	}
//
//	public void setAdminId(String adminId) {
//		this.adminId = adminId;
//	}

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

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	
}
