package com.norbert.customer.user;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserJPAService implements UserDAO {
    private final UserRepository userRepository;

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException("User with email " + email + " doesn't exist"));
    }

    public boolean isUserPresent(String email){
        return userRepository.existsByEmail(email);
    }

    public void save(User user){
        userRepository.save(user);
    }
}
