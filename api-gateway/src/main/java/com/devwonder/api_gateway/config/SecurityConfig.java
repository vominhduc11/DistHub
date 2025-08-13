package com.devwonder.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:8080,http://127.0.0.1:5500,http://localhost:5500,http://localhost:5173}")
    private String allowedOrigins;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/api/auth/**", "/auth/**", "/api/public/**", "/api/auth/.well-known/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/user/resellers").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/user/resellers").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/content/blogs").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/content/blogs/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/content/blogs").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/content/blogs/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/api/content/blogs/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow CORS preflight
                .pathMatchers("/api/adminanduser/**").access(hasAdminAndCustomerRoles()) // Requires both Admin and Customer roles
                .pathMatchers("/api/dealer/**").hasRole("DEALER") // Dealer role required
                .pathMatchers("/api/customer/**").hasRole("CUSTOMER") // Customer role required
                .pathMatchers("/api/auth/validate").authenticated() // Any authenticated user
                .anyExchange().denyAll() // Deny other routes (will return 403 if not found)
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwkSetUri("http://auth-service:8082/auth/.well-known/jwks.json")
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            )
            .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Object rolesObj = jwt.getClaim("roles");
            if (rolesObj instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<String> rolesList = (java.util.List<String>) rolesObj;
                return rolesList.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(java.util.stream.Collectors.toList());
            }
            // Default role if no roles found
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        });
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Parse allowed origins from environment variable
        String[] origins = allowedOrigins.split(",");
        for (String origin : origins) {
            configuration.addAllowedOrigin(origin.trim());
        }
        
        // Also allow localhost patterns for development
        configuration.addAllowedOriginPattern("http://localhost:*");
        configuration.addAllowedOriginPattern("http://127.0.0.1:*");
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight for 1 hour
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private ReactiveAuthorizationManager<AuthorizationContext> hasAdminAndCustomerRoles() {
        return (authentication, context) -> {
            return authentication
                .map(auth -> auth.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(java.util.stream.Collectors.toSet()))
                .map(authorities ->
                    authorities.contains("ROLE_ADMIN") && authorities.contains("ROLE_CUSTOMER"))
                .map(hasRoles -> new org.springframework.security.authorization.AuthorizationDecision(hasRoles));
        };
    }

}
