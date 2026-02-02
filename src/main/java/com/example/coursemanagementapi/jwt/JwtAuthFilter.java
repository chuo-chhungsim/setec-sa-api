package com.example.coursemanagementapi.jwt;

import com.example.coursemanagementapi.service.AppUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Skip JWT validation for certain endpoints
        String requestPath = request.getRequestURI();
        if (shouldSkipJwtValidation(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String email = null;
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                try {
                    email = jwtService.extractEmail(token);
                } catch (Exception e) {
                    // Invalid token format - let it pass through, Spring Security will handle it
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = appUserService.loadUserByUsername(email);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            
            filterChain.doFilter(request, response);
            
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            // Re-throw to be handled by AuthenticationEntryPoint
            throw e;
        } catch (Exception e) {
            // For other exceptions, let Spring Security handle them
            filterChain.doFilter(request, response);
        }
    }
    
    private boolean shouldSkipJwtValidation(String requestPath) {
        return requestPath.equals("/api/v1/auth/login") ||
               requestPath.equals("/api/v1/auth/register") ||
               requestPath.startsWith("/v3/api-docs") ||
               requestPath.startsWith("/swagger-ui") ||
               requestPath.startsWith("/swagger-resources") ||
               requestPath.startsWith("/webjars");
    }
}
