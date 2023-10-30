package com.norbert.customer.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Transactional(readOnly = true)
    @Query("SELECT s FROM User s WHERE s.email = ?1")
    Optional<User> findByEmail(String email);

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(*)>0 THEN TRUE ELSE FALSE END FROM User s WHERE s.email = ?1")
    boolean existsByEmail(String email);
}
