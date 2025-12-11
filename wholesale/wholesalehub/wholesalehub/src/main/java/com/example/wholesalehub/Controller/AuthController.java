package com.example.wholesalehub.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.wholesalehub.Security.otpUtil;
import com.example.wholesalehub.Service.EmailService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.wholesalehub.Entity.LoginRequest;
import com.example.wholesalehub.Entity.Roles;
import com.example.wholesalehub.Entity.SignupRequest;
import com.example.wholesalehub.Entity.Users;
import com.example.wholesalehub.Repository.RoleRepository;
import com.example.wholesalehub.Repository.UserRepository;
import com.example.wholesalehub.Security.JwtUtil;

@RestController
@RequestMapping("/auth")
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
    private  EmailService emailService;

	@PostMapping("/signup")
	public ResponseEntity<String>register(@RequestBody SignupRequest signuprequest){
		if(userrepository.findByEmail(signuprequest.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("username is already exists");
		}
		Users user=new Users();
		user.setEmail(signuprequest.getEmail());
		String encodedpassword=passwordencoder.encode(signuprequest.getPassword());
		user.setPassword(encodedpassword);

		Set<Roles>roles=new HashSet<>();
		for(String rolename:signuprequest.getRoles()) {
			Roles role=rolerepository.findByname(rolename)
					.orElseThrow(()->new RuntimeException("Role Not Found:"+rolename));
					roles.add(role);
		}
		user.setRoles(roles);
		userrepository.save(user);
		return ResponseEntity.ok("User Signup Successfully");

		}
	@PostMapping("/login")
	public ResponseEntity<String>login(@RequestBody LoginRequest loginrequest){
		try {
			authenticationmanager.authenticate(new UsernamePasswordAuthenticationToken(loginrequest.getEmail(),loginrequest.getPassword()));
			String token=jwtutil.generateToken(loginrequest.getEmail());
			return ResponseEntity.ok(token);
	}	catch(Exception e) {
        return ResponseEntity.status(401).body("Invalid username or password");

	}

		}
    private Map<String, String> otpStorage = new HashMap<>(); // (email -> otp)
    private Map<String, Long> otpExpiry = new HashMap<>();// (email -> expiry time)

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        // if(!userRepo.existsByEmail(email)) return "Email not found";

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
            return "OTP verified. You can now reset your password.";
        } else {
            return "Invalid OTP.";
        }
    }

    @PostMapping("/reset-password")
    public Map<String,Object> resetPassword(@RequestParam String email, @RequestParam String newpassword) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(newpassword);


        Users user = userrepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        user.setPassword(hashedPassword);

        userrepository.save(user);

        otpStorage.remove(email);
        otpExpiry.remove(email);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("password successfully reset", newpassword);

        return response;

    }
	}
