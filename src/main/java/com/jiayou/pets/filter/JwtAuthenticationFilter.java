package com.jiayou.pets.filter;

import com.jiayou.pets.utils.JwtUtil;

import io.jsonwebtoken.lang.Arrays;
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

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtUtil.validateToken(token)) {
                Object email = jwtUtil.getClaimFromToken(token, "email");
                Object userId = jwtUtil.getClaimFromToken(token, "userId");
                Object roles = jwtUtil.getClaimFromToken(token, "roles");
                // 如果 token 中没有 roles，可能返回 null，需处理
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
                filterChain.doFilter(request, response);
            }
        } else {
            // 如果 token 无效或缺失，直接放行，交给 Spring Security 处理
            filterChain.doFilter(request, response);
        }
    }
}