package com.duelivotni.carrie.auth.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException {
        log.debug("Unauthorized. Rejecting access");
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, String[]> errors = Collections.singletonMap("errors", new String[]{"Unauthorized"});
        resp.getWriter().write(mapper.writeValueAsString(errors));
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
