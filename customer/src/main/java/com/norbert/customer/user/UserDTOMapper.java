package com.norbert.customer.user;

import java.util.function.Function;

public class UserDTOMapper implements Function<User,UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(user.getEmail(), user.getRole(),user.getSubscriptionUntil(),user.getDecks());
    }
}
