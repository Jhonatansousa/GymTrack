package com.jhonatan.gymtrack.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenUtil tokenUtil;

    private static final String[] SWAGGER_LIST = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/api/v1/auth/**").permitAll()
                        //.requestMatchers("/register").permitAll()
                        .requestMatchers(SWAGGER_LIST).permitAll()
                        .anyRequest().authenticated();
                })
                .addFilterBefore(new AuthFilter(tokenUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // defino que pode acessar a API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        //endereço do angular padrão de desenvolvimento local !!!!TROCAR EM PROD!!!!!
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        //métodos http permitidos
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
        //cabeçalhos permitidos, importante pro Bearer Token
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        //pode enviar cookies ou antenticação via browser se necessario
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
