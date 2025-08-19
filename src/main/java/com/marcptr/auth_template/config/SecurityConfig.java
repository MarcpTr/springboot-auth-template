package com.marcptr.auth_template.config;

import static org.springframework.http.HttpMethod.POST;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import com.marcptr.auth_template.service.UserService;

@Configuration
public class SecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/register", "/login", "/", "/error")
                                                .permitAll()
                                                .requestMatchers(POST, "/register", "/login").permitAll()
                                                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/profile","dashboard").authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID", "remember-me"))
                                .rememberMe(remember -> remember
                                                .rememberMeServices(rememberMeServices)
                                                .key("3(/w8976H86(/%/")
                                                .tokenValiditySeconds(604800)
                                                .alwaysRemember(false));
                return http.build();
        }
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
        @Bean
        public RememberMeServices rememberMeServices(UserService userService) {
                return new PersistentTokenBasedRememberMeServices(
                                "3(/w8976H86(/%/", 
                                userService,
                                new InMemoryTokenRepositoryImpl() 
                );
        }
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
