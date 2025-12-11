package com.example.wholesalehub.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="userprofile")
public class Userprofile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private int profileid;
	
	private String firstname;
	
	private String lastname;
	
	private String email;
	
	private String phonenumber;
	
	private String profilepic;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userid", unique = true)
	@JsonIgnore
	private Users user;

	 @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "shippingaddressid")
	    private Address shippingAddress;

	    @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "billingaddressid")
	    private Address billingAddress;

		public int getProfileid() {
			return profileid;
		}

		public void setProfileid(int profileid) {
			this.profileid = profileid;
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

		public Users getUser() {
			return user;
		}

		public void setUser(Users user) {
			this.user = user;
		}

		public Address getShippingAddress() {
			return shippingAddress;
		}

		public void setShippingAddress(Address shippingAddress) {
			this.shippingAddress = shippingAddress;
		}

		public Address getBillingAddress() {
			return billingAddress;
		}

		public void setBillingAddress(Address billingAddress) {
			this.billingAddress = billingAddress;
		}
	

}
