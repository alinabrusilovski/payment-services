package com.paymentservice.controller;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

@RestController
public class ValidateTokenController {
    private static final Logger logger = LoggerFactory.getLogger(ValidateTokenController.class);

    private final String jwksUri = "http://localhost:11000/.well-known/jwks.json";

    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.error("Authorization header is missing or invalid");
                return ResponseEntity.status(401).body(Map.of("error", "Missing or invalid token"));
            }

            String token = authorizationHeader.substring(7);

            DecodedJWT jwt = validateAndDecodeToken(token);

            List<String> scopes = jwt.getClaim("scope").asList(String.class);
            if (scopes == null || !scopes.contains("admin")) {
                logger.warn("Token is valid, but user is not an admin");
                return ResponseEntity.status(403).body(Map.of("error", "Token is valid, but user is not an admin"));
            }

            logger.info("Token is valid, user is admin");
            return ResponseEntity.ok(Map.of("message", "Token is valid, user is admin"));
        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage(), e);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token: " + e.getMessage()));
        }
    }

    private DecodedJWT validateAndDecodeToken(String token) throws Exception {
        JwkProvider jwkProvider = new UrlJwkProvider(new URL(jwksUri));

        DecodedJWT decodedJWT = JWT.decode(token);

        Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());

        RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);

        algorithm.verify(decodedJWT);

        if (decodedJWT.getExpiresAt().before(new java.util.Date())) {
            logger.warn("Token has expired");
            throw new Exception("Token has expired");
        }

        return decodedJWT;
    }
}
