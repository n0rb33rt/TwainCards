package com.norbert.customer.config;


import com.norbert.customer.jwt.JwtService;
import com.norbert.customer.jwt.JwtToken;
import com.norbert.customer.jwt.JwtTokenJDBCService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogoutService implements LogoutHandler {
    private final JwtTokenJDBCService jwtTokenJDBCService;
    private final JwtService jwtService;
    private final static String UNAUTHORIZED_ERROR_MESSAGE = "{\"error\": \"Need authentication\"}";
    @SneakyThrows
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        if(jwtService.isAuthHeaderValid(authHeader)){
            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(UNAUTHORIZED_ERROR_MESSAGE);
            return;
        }
        jwt = jwtService.extractJwtTokenFromHeader(authHeader);
        JwtToken storedToken = jwtTokenJDBCService.findByToken(jwt).orElse(null);
        if(storedToken != null){
            storedToken.setRevoked(true);
            jwtTokenJDBCService.updateJwtTokenByRevoked(storedToken);
        }
    }

}
