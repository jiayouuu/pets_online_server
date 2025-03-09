package com.jiayou.pets.filter;

import com.jiayou.pets.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 从头中提取
        } else {
            // 从 URL 参数中提取 Token
            token = request.getParameter("token");
        }

        if (token != null && !token.isEmpty() && jwtUtil.validateToken(token)) {
            Object email = jwtUtil.getClaimFromToken(token, "email");
            Object userId = jwtUtil.getClaimFromToken(token, "userId");
            Object roles = jwtUtil.getClaimFromToken(token, "roles");
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (roles instanceof List<?>) {
                List<?> rolesList = (List<?>) roles;
                authorities = rolesList.stream()
                        .filter(item -> item instanceof String)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
            }
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null,
                    authorities);
            auth.setDetails(new HashMap<String, Object>() {
                {
                    put("userId", userId);
                }
            });
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}