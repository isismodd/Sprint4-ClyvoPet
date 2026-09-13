package br.com.fiap.ClyvoPet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {

        this.userDetailsService =
                userDetailsService;

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http

                .authorizeHttpRequests(auth -> auth

                        // ==========================
                        // ROTAS PÚBLICAS
                        // ==========================

                        .requestMatchers(
                                "/login",
                                "/registrar",
                                "/esqueci-senha",
                                "/redefinir-senha",

                                "/api/auth/**",

                                "/css/**",
                                "/js/**",
                                "/webjars/**",
                                "/images/**"
                        )
                        .permitAll()

                        // ==========================
                        // SOMENTE ADMIN
                        // ==========================

                        .requestMatchers(
                                "/web/veterinarios/**",
                                "/api/veterinarios/**",
                                "/web/admin/**"
                        )
                        .hasRole("ADMIN")

                        // ==========================
                        // WEB
                        // ==========================

                        .requestMatchers(
                                "/web/animais/**",
                                "/web/consultas/**",
                                "/web/lembretes/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "VETERINARIO"
                        )

                        // ==========================
                        // API / MOBILE
                        // ==========================

                        .requestMatchers(
                                "/api/animais/**",
                                "/api/consultas/**",
                                "/api/lembretes/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "VETERINARIO"
                        )

                        // ==========================
                        // HOME
                        // ==========================

                        .requestMatchers(
                                "/home",
                                "/"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "VETERINARIO"
                        )

                        .anyRequest()
                        .authenticated()
                )

                // ==========================
                // LOGIN WEB
                // ==========================

                .formLogin(form -> form

                        .loginPage("/login")

                        .defaultSuccessUrl(
                                "/home",
                                true
                        )

                        .permitAll()
                )

                // ==========================
                // LOGOUT WEB
                // ==========================

                .logout(logout -> logout

                        .logoutSuccessUrl(
                                "/login?logout"
                        )

                        .permitAll()
                )

                .userDetailsService(
                        userDetailsService
                )

                // REST API usando JWT
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}