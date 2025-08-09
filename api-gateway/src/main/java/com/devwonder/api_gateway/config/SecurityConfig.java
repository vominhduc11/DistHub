package com.devwonder.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpMethod;

import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable()) // CORS handled by Spring Cloud Gateway
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/api/auth/**", "/api/public/**", "/api/auth/.well-known/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/user/resellers").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/user/resellers").permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow CORS preflight
                .pathMatchers("/api/admin/**").hasRole("ADMIN") // Admin role required
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
            String role = jwt.getClaimAsString("role");
            if (role != null) {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return Collections.emptyList();
        });
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }

}
