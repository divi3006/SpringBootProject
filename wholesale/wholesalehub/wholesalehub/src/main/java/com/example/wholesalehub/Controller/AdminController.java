package com.example.wholesalehub.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wholesalehub.Security.JwtUtil;



@RestController
@RequestMapping("/Admin")
public class AdminController {
	
	
	
	@Autowired
	private JwtUtil jwtutil;
	
	
	@Value("${role.admin}")
	private String roleadmin;
	
	
	@GetMapping("/dashboard")
	public ResponseEntity<String> getAdminData(@RequestHeader("token")  String token) {
	    
	    if(token!=null&&token.startsWith("Bearer ")) {
	    	String jwttoken=token.substring(7);
	    	try {
	    		if(jwtutil.validateToken(jwttoken)) {
	    			String email=jwtutil.extractEmail(jwttoken);
	    	Set<String>roles=jwtutil.extractRoles(jwttoken);
	    			if(roles.contains(roleadmin)) {
	    				return ResponseEntity.ok("Welcome"+email+"here is the"+roles+"specific data");	
	    			}else {
	    				return ResponseEntity.status(403).body("Access Denied");
	    			}
	    		}
	    	}
	    			catch(Exception ex) {
	    				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid Token");	
	    			}
	    		}
	    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization invalid");
	}
	    }
