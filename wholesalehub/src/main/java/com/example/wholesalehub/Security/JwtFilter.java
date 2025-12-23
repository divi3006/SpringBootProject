package com.example.wholesalehub.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.wholesalehub.Service.CustomuserdetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	
	@Autowired
	private JwtUtil jwtutil;
	
	@Autowired
	private CustomuserdetailsService customuserdetailservice;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		System.out.println(request.getHeaderNames());
		String token=request.getHeader("token");
		
		if(token!=null && token.startsWith("Bearer ")) {
			token=token.substring(7);
			
			String email=jwtutil.extractEmail(token);
			if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userdetails=customuserdetailservice.loadUserByUsername(email);
				
				if(jwtutil.validateToken(token)) {
					UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(
					userdetails,null,userdetails.getAuthorities());
					authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authtoken);
				}
			}
		}
	
	filterChain.doFilter(request,response);
	}
}
