package com.norbert.customer.jwt;

import com.norbert.customer.exception.JwtTokenNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtTokenService {
    private final JwtTokenDAO jwtTokenDAO;

    public JwtToken findByToken(String token)  {
        return jwtTokenDAO.findByToken(token).orElseThrow(()->
                new JwtTokenNotFoundException("Bearer token is not valid"));
    }


    public void save(JwtToken jwtToken) {
       jwtTokenDAO.save(jwtToken);
    }

}
