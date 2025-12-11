package com.example.wholesalehub.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.wholesalehub.Entity.Roles;
import com.example.wholesalehub.Entity.Users;
import com.example.wholesalehub.Repository.UserRepository;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Autowired
	private UserRepository userrepository;

    private final SecretKey secretKey;
    private final int jwtExpirationMs;

	 public JwtUtil(@Value("${app.jwt.secret}") String secret,
             @Value("${app.jwt.expiration:7200000}") int jwtExpirationMs) {
  this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  this.jwtExpirationMs = jwtExpirationMs;
}
	// Generate Token
	public String generateToken(String email) {
		Optional<Users> user = userrepository.findByEmail(email);
		Set<Roles> roles = user.get().getRoles();

		return Jwts.builder()//to generates new tokens
				.setSubject(email)//Stores the email in the token.
				.claim("roles", roles.stream()
						.map(Roles::getName)
						.collect(Collectors.joining(",")))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(secretKey, SignatureAlgorithm.HS512)//Sign the token with your secret key
				.compact();//returns the JWT string.
	}

	// Extract email
	public String extractEmail(String token) {
		return Jwts.parser()//it can read and validate the token
				.setSigningKey(secretKey)//Set the secret key used to sign the JWT
				.build()//build the parser
				.parseClaimsJws(token)//Parse the token and verify signature
				.getBody()//Get the payload (claims) of the token
				.getSubject();//Extract the "subject" field, usually email
	}

	// Extract roles
	public Set<String> extractRoles(String token) {
	    String roleString = (String) Jwts.parser()
	            .setSigningKey(secretKey)
	            .build()
	            .parseClaimsJws(token)
	            .getBody()
	            .get("roles");

	    return Set.of(roleString.split(",")).stream()
	            .map(String::trim)       // remove spaces
	            .collect(Collectors.toSet());
	}

	// Validate token
	public boolean validateToken(String token) {
		try {
			Jwts.parser()//it can read and validate the token
					.setSigningKey(secretKey)//The parser uses this key to verify that the token was not tampered with.
					.build()//Builds the parser with the secret key.
					.parseClaimsJws(token);//Parse the token and verify signature
			return true;//If no exception occurs, it means the token is valid, so the method returns true.
		} catch (JwtException e) {
	        System.out.println("Token invalid: " + e.getMessage());
			return false;//invalid, expired, or tampered token
		}
	}
}

