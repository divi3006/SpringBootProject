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

import io.jsonwebtoken.JwtException;



@RestController
@RequestMapping("/admin")
public class AdminController {
	
	
	
	@Autowired
	private JwtUtil jwtutil;

	@Value("${role.admin}")
	private String roleadmin;
	
	@GetMapping("/profile")
	public ResponseEntity<Map<String,Object>> getUserData(
	        @RequestHeader(value="Authorization", required=false) String token) {

	    Map<String,Object> response = new HashMap<>();

	    if(token == null || !token.startsWith("Bearer ")) {
	        response.put("status", "FAILURE");
	        response.put("error", "Authorization header missing or invalid");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }

	    String jwttoken = token.substring(7);

	    try {
	        if(!jwtutil.validateToken(jwttoken)) {
	            response.put("status", "FAILURE");
	            response.put("error", "Token is invalid or expired");
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	        }

	        String email = jwtutil.extractEmail(jwttoken);
	        Set<String> roles = jwtutil.extractRoles(jwttoken);

	        if(roles.stream().anyMatch(r -> r.equalsIgnoreCase(roleadmin))) {
	            response.put("status", "SUCCESS");
	            response.put("message", "Welcome " + email);
	            response.put("roles", roles);
	            return ResponseEntity.ok(response);
	        } else {
	            response.put("status", "FAILURE");
	            response.put("error", "Access Denied: You do not have the required role");
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	        }

	    } catch(Exception ex) {
	        response.put("status", "FAILURE");
	        response.put("error", "Token processing error");
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	    }
	}
}