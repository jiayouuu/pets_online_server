package com.jiayou.pets.config;

import com.jiayou.pets.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    String origin = request.getHeader("Origin");
                    config.setAllowedOrigins(origin != null ? List.of(origin) : List.of("http://localhost:5173")); // 默认值防止 null
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true); // 支持凭证
                    config.setMaxAge(3600L);
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/files/download/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/index.html").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/app/**").permitAll() // STOMP 消息路径豁免，但 /pets/ws 需认证
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        // 明确不豁免 /pets/ws/**，确保经过 JWT 认证
                        .requestMatchers("/pets/ws/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, resp, authEx) -> {
                            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            resp.setContentType("application/json");
                            resp.setCharacterEncoding("UTF-8");
                            resp.getWriter().write("{\"code\": 401, \"message\": \"未授权\", \"data\": \"null\"}");
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}