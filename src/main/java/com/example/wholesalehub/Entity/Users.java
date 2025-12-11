package com.example.wholesalehub.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
public class Users {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userid;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	private String email;
	
	private String password;
	
//	private String adminId;

	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)//Indicates that a user can have many roles, and a role can belong to many users.
	@JoinTable(//connect user and role in one table
		    name = "user_roles",//jointable name
		    joinColumns = @JoinColumn(name = "userid"),//foreign key to user table
		    inverseJoinColumns = @JoinColumn(name = "id"))//foreign key to roles table
	
			private Set<Roles>roles=new HashSet<>();//roles stores in set for avoid duplicates
	
	
	@OneToOne(mappedBy="user")
	private Userprofile userprofile;
	
	

//	public String getAdminId() {
//		return adminId;
//	}
//
//	public void setAdminId(String adminId) {
//		this.adminId = adminId;
//	}

	public Userprofile getUserprofile() {
		return userprofile;
	}

	public void setUserprofile(Userprofile userprofile) {
		this.userprofile = userprofile;
	}

	
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

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}
}
