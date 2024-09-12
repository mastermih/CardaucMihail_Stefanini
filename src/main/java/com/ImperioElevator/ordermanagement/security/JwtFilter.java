//package com.ImperioElevator.ordermanagement.config;
//
//import com.ImperioElevator.ordermanagement.security.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtService jwtService;
//    @Autowired
//     ApplicationContext context
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//         String authHeader = request.getHeader("Authorization");
//         String token = null;
//         String email = null;
//
//
//    if(authHeader != null && authHeader.startsWith("Bearer ")){
//    token = authHeader.substring(7);
//    email = jwtService.extractUserEmail(token);
//    }
//    if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
//
//       UserDetails userDetails = context.getBean(USerDetails.class).loadUserByUsername(email); //// Must be renamed as loadUserByEmail
//
//        if(jwtService.validateToken(token, userDetails)){
//
//        }
//    }
//    }
//}
