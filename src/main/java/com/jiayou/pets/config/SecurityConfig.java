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
                    config.setAllowedOrigins(List.of("*")); // 允许所有来源，与 CorsConfig 一致
                    config.setAllowedMethods(List.of("*")); // 允许所有方法
                    config.setAllowedHeaders(List.of("*")); // 允许所有请求头
                    config.setAllowCredentials(false);      // 不允许携带 Cookie，与 CorsConfig 一致
                    config.setMaxAge(3600L);                // 预检请求缓存时间 3600 秒
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/files/download/**").permitAll()
//                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/index.html").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/app/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/assets/**").permitAll()
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