package com.example.wholesalehub.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.wholesalehub.Dto.LoginRequest;
import com.example.wholesalehub.Dto.SignupRequest;
import com.example.wholesalehub.Entity.Roles;
import com.example.wholesalehub.Entity.Users;
import com.example.wholesalehub.Repository.RoleRepository;
import com.example.wholesalehub.Repository.UserRepository;
import com.example.wholesalehub.Security.JwtUtil;
import com.example.wholesalehub.Security.OtpUtil;
import com.example.wholesalehub.Service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:5173/")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationmanager;
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private RoleRepository rolerepository;
	@Autowired
	private PasswordEncoder passwordencoder;
	@Autowired
	private JwtUtil jwtutil;
	@Autowired
	private OtpUtil otpUtil;
	@Autowired
	private EmailService emailService;
	
	
	@Value("${role.admin}")
	private String adminRoleName;
	
	@Value("${role.user}")
	private String userRoleName;

	@PostMapping("/signup")
	public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody SignupRequest signupRequest) {
	    Map<String, Object> response = new HashMap<>();

	    //  Check if email already exists
	    if (userrepository.findByEmail(signupRequest.getEmail()).isPresent()) {
	        response.put("status", "error");
	        response.put("message", "Email is already exists");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	    // Validate roles
	    if (signupRequest.getRoles() == null || signupRequest.getRoles().isEmpty()) {
	        response.put("status", "error");
	        response.put("message", "At least one role must be selected");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    //  Create user object
	    Users user = new Users();
	    user.setEmail(signupRequest.getEmail());
	    user.setPassword(passwordencoder.encode(signupRequest.getPassword()));

	    // Assign roles (validate each one)
	    Set<Roles> roles = new HashSet<>();
	    for (String rolename : signupRequest.getRoles()) {
	        Roles role = rolerepository.findByName(rolename).orElse(null);
	        if (role == null) {
	            response.put("status", "error");
	            response.put("message", "Role not found: " + rolename);
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	    roles.add(role);
	    } 

	    user.setRoles(roles);

	    // Save user
	    userrepository.save(user);

	    // Success response
	    response.put("status", "success");
	    response.put("message", "User Registered Successfully");
	    return ResponseEntity.ok(response);
	}



	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginrequest) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        authenticationmanager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                loginrequest.getEmail(), 
	                loginrequest.getPassword()
	            )
	        );
	        String token = jwtutil.generateToken(loginrequest.getEmail());
	        response.put("status", "success");
	        response.put("token", token);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "Invalid email or password");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	}

		 private Map<String, String> otpStorage = new HashMap<>(); // (email -> otp)
		    private Map<String, Long> otpExpiry = new HashMap<>();// (email -> expiry time)

		    @PostMapping("/forgot-password")
		    public String forgotPassword(@RequestParam String email) {

		        String otp = otpUtil.generateOtp();
		        otpStorage.put(email, otp);
		        otpExpiry.put(email, System.currentTimeMillis() + 5*60*1000); // 5 min expiry

		        emailService.sendOtpEmail(email, otp);

		        return "OTP sent to your email.";
		    }
		    
		    @PostMapping("/verify-otp")
		    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
		        if(!otpStorage.containsKey(email)) {
		            return "No OTP generated for this email.";
		        }

		        if(System.currentTimeMillis() > otpExpiry.get(email)) {
		            return "OTP expired!";
		        }

		        if(otp.equals(otpStorage.get(email))) {
		            otpStorage.remove(email);
		            otpExpiry.remove(email);
		            return "OTP verified. You can now reset your password.";
		        } else {
		            return "Invalid OTP.";
		        }
		    }

		    @PostMapping("/reset-password")
		    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam String email, @RequestParam String newpassword) {
		        Map<String, Object> response = new HashMap<>();

		        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		        String hashedPassword = encoder.encode(newpassword);

		        Users user = userrepository.findByEmail(email).orElse(null);
		        if (user == null) {
		            response.put("status", "error");
		            response.put("message", "User not found with email: " + email);
		            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		        }


		        user.setPassword(hashedPassword);

		        userrepository.save(user);

		        otpStorage.remove(email);
		        otpExpiry.remove(email);

		        response.put("status", "success");
		        response.put("password successfully reset", newpassword);

		        return ResponseEntity.ok(response);

		    }
			}