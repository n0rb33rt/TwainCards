package com.norbert.customer.config;


import com.norbert.customer.jwt.JwtService;
import com.norbert.customer.exception.JwtTokenNotFoundException;
import com.norbert.customer.exception.JwtTokenRevokedException;
import com.norbert.customer.user.User;
import com.norbert.customer.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final static String TOKEN_EXPIRED_ERROR_MESSAGE = "{\"error\": \"Token has expired\"}";
    private final static String TOKEN_INVALID_ERROR_MESSAGE = "{\"error\": \"Token is not valid\"}";


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtService.isAuthHeaderValid(authHeader)) {
            filterChain.doFilter(request,response);
            return;
        }
        final String jwtToken = jwtService.extractJwtTokenFromHeader(authHeader);
        authenticateUserIfRequired(jwtToken,new FilterContext(request,response,filterChain));
    }
    private void authenticateUserIfRequired(
            String jwtToken,
            FilterContext filterContext
    ) throws IOException, ServletException {
        try {
            if (isUserEmailIsPresentAndNotAuthenticated(jwtToken))
                setAuthentication(jwtToken, filterContext.request());
            filterContext.filterChain().doFilter(filterContext.request(),filterContext.response());
        } catch (MalformedJwtException | JwtTokenNotFoundException | JwtTokenRevokedException e) {
            handleTokenError(filterContext.response(), TOKEN_INVALID_ERROR_MESSAGE);
        } catch (ExpiredJwtException e) {
            handleTokenError(filterContext.response(), TOKEN_EXPIRED_ERROR_MESSAGE);
        }
    }
    private boolean isUserEmailIsPresentAndNotAuthenticated(String userEmail) {
        return userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void setAuthentication(String jwtToken, HttpServletRequest request) {
        final String email = jwtService.extractEmail(jwtToken);
        User user = userService.findUserByEmail(email);
        if (jwtService.isTokenValid(jwtToken)) {
            SecurityContextHolder.getContext().setAuthentication(buildAuthToken(user, request));
        }else{
            throw new JwtTokenRevokedException("Bearer token is revoked");
        }
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(
            User user,
            HttpServletRequest request
    ) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
               authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }

    private void handleTokenError(
            HttpServletResponse response,
            String errorMessage
    ) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(errorMessage);
    }

}
