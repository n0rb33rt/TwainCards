package com.norbert.customer.user;

public interface UserDAO {
    void save(User user);
    boolean isUserPresent(String email);
    User findUserByEmail(String email);

}
