package com.paymentservice.filter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final String jwksUri = "http://localhost:11000/.well-known/jwks.json";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        logger.debug("Entered JwtAuthenticationFilter");


        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            response.getWriter().flush();
            return;
        }

        try {
            String token = authorizationHeader.substring(7);
            DecodedJWT jwt = validateAndDecodeToken(token);

            List<String> scopes = jwt.getClaim("scope").asList(String.class);
            logger.info("Token valid for user: {}", jwt.getSubject());
            logger.info("Token roles (scopes): {}", scopes);

            if (scopes != null) {
                List<SimpleGrantedAuthority> authorities = scopes.stream()
                        .map(scope -> new SimpleGrantedAuthority("ROLE_" + scope.toUpperCase()))
                        .collect(Collectors.toList());

                if (scopes.contains("admin")) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                } else {
                    logger.warn("Token is valid, but user does not have the required admin role");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Access Denied: You do not have the required admin role\"}");
                    response.getWriter().flush();
                }
            } else {
                logger.warn("Token is valid, but no roles found");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Access Denied: No roles found in the token\"}");
                response.getWriter().flush();
            }
        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token: " + e.getMessage() + "\"}");
            response.getWriter().flush();
        }
    }

    private DecodedJWT validateAndDecodeToken(String token) throws Exception {
        JwkProvider jwkProvider = new UrlJwkProvider(new URL(jwksUri));
        DecodedJWT decodedJWT = JWT.decode(token);
        Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());
        RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        algorithm.verify(decodedJWT);
        return decodedJWT;
    }
}