package com.example.wholesalehub.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import io.jsonwebtoken.JwtException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.wholesalehub.Dto.AddressRequest;
import com.example.wholesalehub.Dto.ChangePasswordRequest;
import com.example.wholesalehub.Dto.UserprofileRequest;
import com.example.wholesalehub.Dto.UserprofileResponse;
import com.example.wholesalehub.Entity.Userprofile;
import com.example.wholesalehub.Security.JwtUtil;
import com.example.wholesalehub.Service.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {
	@Autowired
	private JwtUtil jwtutil;
	
	@Autowired
	private UserService userservice;
	

	@Value("${role.user}")
	private String roleuser;
	
	private static final Logger logger=LoggerFactory.getLogger(UserController.class);
	
	
	@GetMapping("/profile")
	public ResponseEntity<String> getUserData(@RequestHeader("Authorization")  String token) {
	    
	    if(token!=null && token.startsWith("Bearer ")) {
	    	String jwttoken=token.substring(7);
	    	try {
	    		if(jwtutil.validateToken(jwttoken)) {
	    			String email=jwtutil.extractEmail(jwttoken);
	     	Set<String>roles=jwtutil.extractRoles(jwttoken);
	     	
	    			if(roles.stream().anyMatch(r -> r.equalsIgnoreCase(roleuser))){
	    				return ResponseEntity.ok("Welcome  " + email + ", you are logged in as " + roles);	
	    			}else {
	    				return ResponseEntity.status(403).body("Access Denied:You do not have the required role to access this resource");
	    			}
	    		}
	    	}
	    			catch(Exception ex) {
	    				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or expired");	
	    			}
	    		}
	    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
	}




	
	//upload profilepic
	 @PostMapping("/uploadProfilePic")
	    public ResponseEntity<Map<String, String>> uploadProfilePic(@RequestParam("file") MultipartFile file) {
	        Map<String, String> response = new HashMap<>();
	        try {
	            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	            Path path = Paths.get("uploads/profilePics/" + fileName);
	            Files.createDirectories(path.getParent());
	            Files.write(path, file.getBytes());

	            response.put("profilePicPath", "/uploads/profilePics/" + fileName);
	            response.put("message", "File uploaded successfully");
	            return ResponseEntity.ok(response);
	        } catch (IOException e) {
	            response.put("message", "File upload failed: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
	 
	 //create user profile
	@PostMapping("/create")
	
	public ResponseEntity<Map<String,Object>>createprofile(@RequestParam int userid,@Valid @RequestBody UserprofileRequest userprofilerequest){
	Map<String,Object>response=new HashMap<>();
	
	try{
		Userprofile userprofile=userservice.createprofile(userid,userprofilerequest);
        logger.info("UserProfile created for userId {}: {}", userid, userprofile);
	
	    response.put("status", "SUCCESS");
        response.put("message", "Profile created successfully");

	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	} catch (IllegalArgumentException e) {
        // If userId invalid or other validation fails
        response.put("status", "FAILURE");
        response.put("error","Invalid request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } catch (Exception e) {
        // Any unexpected error
        response.put("status", "FAILURE");
        response.put("error", "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
	    //Get userprofile
    
	    @GetMapping("/Getprofile")
	    
	    public ResponseEntity<Map<String,Object>>getprofile(@RequestParam int userid){
	    	Map<String,Object>response=new HashMap<>();
	    	try {
	            // Service call

	            UserprofileResponse profileresponse = userservice.getUserProfile(userid);

	            // Logging success
	            logger.info("User profile fetched successfully for userId: {}", userid);

	            // Response body
	            response.put("status", "SUCCESS");
	            response.put("message", "User profile fetched successfully");
	            response.put("profile",profileresponse);

	            return ResponseEntity.ok(response);
	    } catch (IllegalArgumentException e) {
            logger.warn("User profile not found for userId {}: {}", userid, e.getMessage());
            response.put("status", "FAILURE");
            response.put("error", "Invalid request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            logger.error("Error while fetching user profile for userId {}: {}", userid, e.getMessage());
            response.put("status", "FAILURE");
            response.put("error", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  	
	    }
	    }
	    //update profilepic
	    

	    @PutMapping("/updateProfilePic/{userId}")
	    public ResponseEntity<Map<String, Object>> updateProfilePic(
	            @PathVariable int userId,
	            @RequestParam("file") MultipartFile file) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            String uploadedFilePath = userservice.updateProfilePic(userId, file);

	            response.put("status", "SUCCESS");
	            response.put("message", "Profile picture updated successfully");
	            response.put("filePath", uploadedFilePath);

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {
	            logger.error("Error updating profile picture for userId {}: {}", userId, e.getMessage(), e);

	            response.put("status", "FAILURE");
	            response.put("message", "Failed to update profile picture");
	            response.put("filePath", null);

	            return ResponseEntity.status(500).body(response);
	        
	        }   
}
	    @PutMapping("/change-password/{userId}")
	    public ResponseEntity<Map<String, Object>> changePassword(
	            @PathVariable int userId,
	            @Valid @RequestBody ChangePasswordRequest request) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            userservice.changePassword(userId, request);

	            response.put("status", "SUCCESS");
	            response.put("message", "Password changed successfully");

	            return ResponseEntity.ok(response);

	        } catch (IllegalArgumentException e) {
	            response.put("status", "FAILURE");
	            response.put("error", "Invalid request");

	            return ResponseEntity.badRequest().body(response);
	        }
	    }
	    @PutMapping("/update-shipping-address/{userId}")
	    public ResponseEntity<Map<String,Object>> updateShippingAddress(
	            @PathVariable int userId,
	            @Valid @RequestBody AddressRequest request) {
	    	Map<String,Object>response=new HashMap<>();
	        try {
	            userservice.updateShippingAddress(userId, request);
	            response.put("status", "SUCCESS");
	            response.put("message", "Shipping address updated successfully");
	            return ResponseEntity.ok(response);

	            
	        } catch (IllegalArgumentException e) {
	        	response.put("status", "FAILURE");
	            response.put("error", "Invalid request");
	    }
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	    
	    //createOrder
	    
//	    @PostMapping("/createorder")
//	    public ResponseEntity<Map<String,Object>>createoder(@Valid @RequestBody Orderreqeust orderrequest){
//	    	Map<String,Object>repsponse=new HashMap<>();
//	    	
//	   
//	    }
	    
}

