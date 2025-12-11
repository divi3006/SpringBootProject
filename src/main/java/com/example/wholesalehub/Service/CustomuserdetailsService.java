package com.example.wholesalehub.Service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.wholesalehub.Entity.Users;
import com.example.wholesalehub.Repository.UserRepository;

@Service
public class CustomuserdetailsService implements UserDetailsService {//
	//Implements UserDetailsService → Spring Security interface used for loading user data during authentication.
	@Autowired
	private UserRepository userRepository;

	@Override//This method is called automatically by Spring Security when a user tries to log in.
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Users user=userRepository.findByEmail(email)//search user by email if the user is not found it throw error
	    .orElseThrow(()->new UsernameNotFoundException("User Not Found:"+email));
		
		//convert Role to sping security authority
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				user.getRoles().stream()//get all the roles of the user
				.map(role->new SimpleGrantedAuthority(role.getName()))//convert each roles to spring security authority
				.collect(Collectors.toList()));
	}

}//This class loads user info from DB and converts it into a Spring Security UserDetails object.

//Spring Security uses this UserDetails object to:


//Check the password.

//Check authorities (roles) for access control.
