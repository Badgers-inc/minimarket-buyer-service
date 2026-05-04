package org.badgers.buyerservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader("X-Trace-ID");
        if (traceId == null || traceId.equals("")) {
            traceId = UUID.randomUUID().toString();
        }
        try {
            MDC.put("X-Trace-ID", traceId);
            response.addHeader("X-Trace-ID", traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
