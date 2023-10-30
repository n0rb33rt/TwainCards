package com.norbert.customer.jwt;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JwtTokenJDBCService implements JwtTokenDAO{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<JwtToken> findByToken(String token) {
        String sql = "SELECT * FROM jwt_token WHERE token = ?";
        List<JwtToken> results = jdbcTemplate.query(sql, new Object[]{token}, (rs, rowNum) -> {
            JwtToken jwtToken = new JwtToken();
            jwtToken.setId(rs.getLong("id"));
            jwtToken.setToken(rs.getString("token"));
            jwtToken.setRevoked(rs.getBoolean("revoked"));
            return jwtToken;
        });

        if (!results.isEmpty()) {
            return Optional.of(results.get(0));
        } else {
            return Optional.empty();
        }
    }
    @Override
    public void save(JwtToken jwtToken) {
        String sql = "INSERT INTO jwt_token (token, revoked, user_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, jwtToken.getToken(), jwtToken.isRevoked(), jwtToken.getUser().getId());
    }
    @Override
    public void updateJwtTokenByRevoked(JwtToken jwtToken) {
        String sql = "UPDATE jwt_token SET revoked = ? WHERE id = ?";
        jdbcTemplate.update(sql, jwtToken.isRevoked(), jwtToken.getId());
    }

}
