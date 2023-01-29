package com.duelivotni.carrie.auth.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationAccessDeniedHandler
        implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.warn("User: {} attempted to access the protected URL: {}", auth.getName(), request.getRequestURI());
        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        Map<String, String[]> errors = Collections.singletonMap("errors", new String[] {"Access forbidden"});
        response.getWriter().write(mapper.writeValueAsString(errors));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
