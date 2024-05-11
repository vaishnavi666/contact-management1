package com.example.demo.config;

import com.example.demo.model.PersonRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(antMatcher("/swagger-ui/**"),
                                    antMatcher("/swagger-ui.html"),
                                    antMatcher("/h2-console/**"),
                                    antMatcher("/index.html")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.GET, "/contacts")).authenticated()
                            .requestMatchers(antMatcher(HttpMethod.GET, "/contacts/**")).authenticated()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/contacts/**")).hasRole(PersonRole.ADMIN.name())
                            .requestMatchers(antMatcher(HttpMethod.DELETE, "/contacts/**")).hasRole(PersonRole.ADMIN.name())
                            .requestMatchers(antMatcher(HttpMethod.GET, "/persons")).hasRole(PersonRole.ADMIN.name())
                            .requestMatchers(antMatcher(HttpMethod.POST, "/persons")).hasRole(PersonRole.ADMIN.name())
                            .anyRequest().authenticated();
                })
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // This so embedded frames in h2-console are working
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
