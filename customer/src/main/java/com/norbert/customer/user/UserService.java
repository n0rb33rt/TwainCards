package com.norbert.customer.user;

import com.norbert.customer.user.request.PasswordUpdatingRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email){
        return userDAO.findUserByEmail(email);
    }

    public boolean isUserPresent(String email){
        return userDAO.isUserPresent(email);
    }

    public void save(User user){
        userDAO.save(user);
    }

    public UserDTO getUserProfile(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTOMapper userDTOMapper = new UserDTOMapper();
        return userDTOMapper.apply(user);
    }

    public void updatePassword(PasswordUpdatingRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String newHashedPassword = passwordEncoder.encode(request.password());
        user.setPassword(newHashedPassword);
        save(user);
    }
}
