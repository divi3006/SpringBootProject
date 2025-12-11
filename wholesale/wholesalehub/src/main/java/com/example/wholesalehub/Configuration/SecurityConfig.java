package com.example.wholesalehub.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.wholesalehub.Security.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	private JwtFilter jwtfilter;
	
	@Bean
	public SecurityFilterChain securityfilterchain(HttpSecurity http) throws Exception {
	    return http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/auth/**",
	                "/swagger-ui.html",
	                "/swagger-ui/**",
	                "/v3/api-docs/**"
	            ).permitAll()
	              .requestMatchers("/admin/**").hasAuthority("ADMIN")
	            .requestMatchers("/user/**").hasAuthority("USER")
	            .anyRequest().authenticated()
	        )
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
	        .build();
	}
       @Bean
       public PasswordEncoder passwordencoder() {
    	   return new BCryptPasswordEncoder();
       }
       
       @Bean
       public AuthenticationManager authenticationmanager
       (AuthenticationConfiguration configuration)throws
       Exception{
    	   return configuration.getAuthenticationManager();
       }
}
