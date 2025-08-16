package com.proyecto.config;

import com.proyecto.security.LoginSuccessHandler;
import com.proyecto.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

@Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           DaoAuthenticationProvider authProvider) throws Exception {
        http.authenticationProvider(authProvider);

        http
          .authorizeHttpRequests(auth -> auth
            // Público
            .requestMatchers(
              "/", "/index", "/login", "/registro", "/olvide/**",
              "/productos", "/carrito/**",
              "/webjars/**", "/css/**", "/js/**", "/images/**", "/locale/**"
            ).permitAll()

            // Admin
            .requestMatchers("/admin/**").hasRole("ADMIN")

            // Cliente autenticado
            .requestMatchers("/mi-cuenta/**", "/checkout/**").hasRole("CLIENTE")

            // resto
            .anyRequest().authenticated()
          )
          .formLogin(login -> login
            .loginPage("/login").permitAll()
            .successHandler(loginSuccessHandler)
          )
          .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/").permitAll())
          .exceptionHandling(ex -> ex.accessDeniedPage("/errores/403"))
          .csrf(Customizer.withDefaults());

        return http.build();
    }
}
