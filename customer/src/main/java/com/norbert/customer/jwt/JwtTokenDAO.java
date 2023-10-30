package com.norbert.customer.jwt;

import java.util.Optional;

public interface JwtTokenDAO {
    Optional<JwtToken> findByToken(String token);
    void save(JwtToken jwtToken);
    void updateJwtTokenByRevoked(JwtToken jwtToken);
}
