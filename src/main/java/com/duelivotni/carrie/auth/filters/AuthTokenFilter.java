package com.duelivotni.carrie.auth.filters;

import com.duelivotni.carrie.auth.services.TokenService;
import com.duelivotni.carrie.exception.ServiceException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter
        extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                String token = null;
                if (authHeader.startsWith("Bearer ") ) {
                    token = authHeader.replace("Bearer ", "");
                }

                if (authHeader.startsWith("Token ")) {
                    token = authHeader.replace("Token ", "");
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(tokenService.securityUser(token));
                }
            }
            chain.doFilter(request, response);
        } catch (ServiceException e) {
            response.setStatus(e.getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("errors", e.getMessages())));
        }
    }
}