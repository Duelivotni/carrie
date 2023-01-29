package com.duelivotni.carrie.auth.configurations;

import com.duelivotni.carrie.auth.exceptionhandler.AuthorizationAccessDeniedHandler;
import com.duelivotni.carrie.auth.exceptionhandler.UnauthorizedEntryPoint;
import com.duelivotni.carrie.auth.filters.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration  {

    private final UserDetailsService userService;
    private final AuthTokenFilter filter;
    private final UnauthorizedEntryPoint entryPoint;
    private final AuthorizationAccessDeniedHandler handler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh-token").permitAll()
                .antMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
//                        "/reports/**",
                        "/locations/checker/**"
                ).permitAll()
                .anyRequest().authenticated();

        httpSecurity.exceptionHandling().authenticationEntryPoint(entryPoint).accessDeniedHandler(handler);
        httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers().cacheControl();
        httpSecurity.userDetailsService(userService);

        return httpSecurity.build();
    }


}
