package com.example.SentiStock_backend.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null
                && tokenProvider.validateToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            var authentication = tokenProvider.getAuthentication(token);

            if (authentication instanceof UsernamePasswordAuthenticationToken authToken) {
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
  
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); 
        }
        return null;
    }

    // 이 필터를 아예 거치지 않을 경로 지정 (선택)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/api/auth/login")
            || path.equals("/api/auth/signup")
            || path.equals("/api/auth/reissue")
            || path.startsWith("/swagger-ui")
            || path.startsWith("/v3/api-docs");
    }
}
