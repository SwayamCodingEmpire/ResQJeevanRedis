package com.spm.resqjeevanredis.config;

import com.spm.resqjeevanredis.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final String[] PUBLIC_URLS = {
            "/public/**",
            "/server1/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
    }

// A Java configuration snippet using Spring Security to set up a SecurityFilterChain for handling authentication and authorization.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(authorize->{
            authorize.requestMatchers(PUBLIC_URLS).permitAll();
            authorize.requestMatchers("/super-admin/**").permitAll();
            authorize.requestMatchers("/control-room/**").hasAnyRole("CONTROL_ROOM","SUPER_ADMIN");
            authorize.requestMatchers("/resource-depot/**").hasAnyRole("RESOURCE_DEPOT","CONTROL_ROOM","SUPER_ADMIN");
            authorize.requestMatchers("/leader/**").hasAnyRole("LEADER","CONTROL_ROOM","SUPER_ADMIN","REGISTRAR");
            authorize.requestMatchers("/registrar/**").hasAnyRole("REGISTRAR","CONTROL_ROOM","SUPER_ADMIN");
            authorize.anyRequest().authenticated();
        }).sessionManagement(session->{
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }).authenticationProvider(authenticationProvider)
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500","http://127.0.0.1:5501","http://127.0.0.1:8080"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(source);
    }

//    @Bean
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/swagger-ui/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
//                .resourceChain(false);
//    }
}
