package com.norbert.customer.config;

import com.norbert.customer.exception.BadRequestException;
import com.norbert.customer.user.User;
import com.norbert.customer.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class AuthProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override // todo: refactor
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User user;
        try {
            user = userService.findUserByEmail(email);
        } catch (UsernameNotFoundException e){
            throw new BadCredentialsException("Email or password is wrong");
        }


        if(!user.getSetPassword())
            throw new BadRequestException("You need to set your password up by signing in by google."); // todo: bad request
        if(passwordEncoder.matches(password,user.getPassword())){
            System.out.println(user.getEnabled());
            if(!user.getEnabled())
                throw new DisabledException("Confirm your email before singing in");
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
            return new UsernamePasswordAuthenticationToken(email, password, authorities);
        } else {
            throw new BadCredentialsException("Email or password is wrong");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
