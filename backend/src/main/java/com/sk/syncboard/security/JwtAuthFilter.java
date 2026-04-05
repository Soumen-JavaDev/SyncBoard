package com.sk.syncboard.security;


import com.sk.syncboard.service.CustomUserDetailsService;
import com.sk.syncboard.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter  extends OncePerRequestFilter {
   @Autowired
    JWTUtil jwtUtil;
   @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String authHeader=request.getHeader("Authorization");
       String token=null;
       String email=null;
       if(authHeader !=null && authHeader.startsWith("Bearer")){
           token=authHeader.substring(7);
           email=jwtUtil.extractEmail(token);
       }
       if(email != null && SecurityContextHolder.getContext().getAuthentication()==null){
          // get user using email
           UserDetails userDetails=customUserDetailsService.loadUserByUsername(email);

           if(jwtUtil.validateToken(email,userDetails,token)){
               UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authToken);

           }
        }
       filterChain.doFilter(request,response);
    }
}
