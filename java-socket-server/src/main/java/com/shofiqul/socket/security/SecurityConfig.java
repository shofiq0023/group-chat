package com.shofiqul.socket.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authProvider;
    private final String[] whiteListUrls = {"/login", "/signup"};

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http, SecurityFilter securityFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(whiteListUrls).permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(new ReactiveAuthenticationManagerAdapter(new ProviderManager(authProvider)))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(securityFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
