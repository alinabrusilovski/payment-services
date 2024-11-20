package com.paymentservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelationIdFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RelationIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String relationId = request.getHeader("relationId");

        if (relationId != null) {
            MDC.put("relationId", relationId);
        } else {
            String generatedRelationId = UUID.randomUUID().toString();
            MDC.put("relationId", generatedRelationId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
