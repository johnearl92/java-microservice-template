package org.example.orderservice.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MDCLoggingFilter implements Filter {
    public static final String TRACE_ID = "TRACE_ID";
    public static final String REQUEST_METHOD = "REQUEST_METHOD";
    public static final String REQUEST_URI = "REQUEST_URI";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Here you can set up MDC context, for example:
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;


            MDC.put(REQUEST_URI, httpRequest.getRequestURI());
            MDC.put(REQUEST_METHOD, httpRequest.getMethod());
            MDC.put(TRACE_ID, UUID.randomUUID().toString());

            chain.doFilter(request, response); // Proceed with the request


        } finally {
            MDC.clear(); // Prevent memory leaks in thread pools
        }
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
}
