package com.example.wholesalehub.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserprofileRequest {
	
	@NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
	private String firstname;
	
	@NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
	private String lastname;
	
	@NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
	private String email;
	
	@NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phonenumber;
	
	@NotBlank(message = "Profile picture URL is required")
	private String profilepic;
	

    @Valid
	private AddressRequest shippingAddress;
	
    @Valid
	private AddressRequest billingAddress;
	
    public AddressRequest getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressRequest shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public AddressRequest getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressRequest billingAddress) {
		this.billingAddress = billingAddress;
	}


	 

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhonenumber() {
			return phonenumber;
		}

		public void setPhonenumber(String phonenumber) {
			this.phonenumber = phonenumber;
		}

		public String getProfilepic() {
			return profilepic;
		}

		public void setProfilepic(String profilepic) {
			this.profilepic = profilepic;
		}

		

}
