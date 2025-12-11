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

	// ✅ Constructor injection for stable secret
	public JwtUtil(@Value("${app.jwt.secret}") String secret,
				   @Value("${app.jwt.expiration:7200000}") int jwtExpirationMs) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.jwtExpirationMs = jwtExpirationMs;
	}

	// Generate Token
	public String generateToken(String email) {
		Optional<Users> user = userrepository.findByEmail(email);
		Set<Roles> roles = user.get().getRoles();

		return Jwts.builder()
				.setSubject(email)
				.claim("roles", roles.stream()
						.map(Roles::getName)
						.collect(Collectors.joining(",")))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(secretKey, SignatureAlgorithm.HS512)
				.compact();
	}

	// Extract email
	public String extractEmail(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	// Extract roles
	public Set<String> extractRoles(String token) {
		String roleString = (String) Jwts.parser()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("roles");
		return Set.of(roleString.split(","));
	}

	// Validate token
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}
}
