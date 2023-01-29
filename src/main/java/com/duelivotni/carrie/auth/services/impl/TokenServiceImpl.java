package com.duelivotni.carrie.auth.services.impl;

import com.duelivotni.carrie.auth.configurations.JwtConfiguration;
import com.duelivotni.carrie.auth.models.dtos.SecurityUser;
import com.duelivotni.carrie.auth.models.enums.TokenType;
import com.duelivotni.carrie.auth.services.TokenService;
import com.duelivotni.carrie.exception.Exceptional;
import com.duelivotni.carrie.exception.ServiceException;
import com.duelivotni.carrie.user.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.duelivotni.carrie.constants.Constants.TOKEN_PREFIX;
import static com.duelivotni.carrie.constants.Messages.TOKEN_EXPIRED;
import static com.duelivotni.carrie.constants.Messages.TOKEN_INVALID;
import static com.duelivotni.carrie.user.models.enums.Role.ROLE_CRAWLER;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl
        implements TokenService, Exceptional {

    private final JwtConfiguration jwt;


    private final UserService userService;

    @Override
    public String generateAccessToken(
            UUID id,
            String email,
            String role,
            String location) {
        try {
            Claims claims = Jwts.claims().setSubject(email);
            claims.put("id", id);
            claims.put("email", email);
            claims.put("location", location);
            claims.put("role", role);
            claims.put("type", TokenType.ACCESS);

            return Jwts.builder()
                    .setSubject(email)
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + jwt.getExpiration()))
                    .signWith(SignatureAlgorithm.HS512, jwt.getSecret().getBytes(StandardCharsets.UTF_8))
                    .compact();
        } catch (Exception e) {
            throw unauthorized("Unable to generate token", e.getMessage());
        }
    }

    @Override
    public String generateRefreshToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("type", TokenType.REFRESH);

        return Jwts.builder()
                .setSubject(email)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + (jwt.getExpiration() * 2)))
                .signWith(SignatureAlgorithm.HS512, jwt.getSecret().getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    @Override
    public String getEmailFromRefreshToken(String refreshToken) {
        return parse(refreshToken).getSubject();
    }

    public Claims parse(String token) {
        try {
            token = token.replace(TOKEN_PREFIX, "");
            return Jwts.parser()
                    .setSigningKey(jwt.getSecret().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            throw unauthorized(TOKEN_INVALID, e.getMessage());
        }
    }

    @Override
    public String email(String token) {
        return email(parse(token));
    }

    @Override
    public Long id(String token) {
        return id(parse(token));
    }

    @Override
    public String role(String token) {
        return role(parse(token));
    }

    @Override
    public Optional<String> location(String token) {
        return location(parse(token));
    }

    @Override
    public String email(Claims claims) {
        return claims.getSubject();
    }

    @Override
    public Long id(Claims claims) {
        return Long.parseLong(claims.get("id").toString());
    }

    @Override
    public String role(Claims claims) {
        return claims.get("role").toString();
    }

    @Override
    public Optional<String> location(Claims claims) {
        return Optional.ofNullable(claims.get("location")).map(Objects::toString);
    }

    @Override
    public SecurityUser securityUser(String token) {
        try {
            Claims claims = parse(token);

            var type = tokenType(token);

            if (isRefreshToken(type)) {
                throw unauthorized(TOKEN_INVALID);
            }

            var roleStr = role(claims);

            if (isExpired(claims.getExpiration()) && isNotCrawler(roleStr)) {
                throw unauthorized(TOKEN_EXPIRED);
            }

            var role = new SimpleGrantedAuthority(roleStr);
            var id = id(claims);
            var email = email(claims);
            var location = userService.getById(id).getLocation();

            return new SecurityUser(email, null, Collections.singleton(role), id, role, email, location);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(TOKEN_INVALID, e);
            throw unauthorized(TOKEN_INVALID);
        }
    }

    private boolean isRefreshToken(TokenType type) {
        return Objects.equals(type, TokenType.REFRESH);
    }

    private boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }

    private boolean isNotCrawler(String role) {
        return !Objects.equals(role, ROLE_CRAWLER.toString());
    }

    private Date expirationDate(String token) {
        Date expiration;
        try {
            final Claims claims = parse(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    @Override
    public TokenType tokenType(String token) {
        return tokenType(parse(token));
    }

    @Override
    public TokenType tokenType(Claims claims) {
        return TokenType.valueOf(claims.get("type").toString());
    }
}
