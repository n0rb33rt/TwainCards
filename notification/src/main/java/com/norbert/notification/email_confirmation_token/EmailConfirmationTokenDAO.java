package com.norbert.notification.email_confirmation_token;

import java.util.Optional;

public interface EmailConfirmationTokenDAO {
    void save(EmailConfirmationToken emailConfirmationToken);
    void enableAccount(String email);
    Optional<EmailConfirmationToken> findByToken(String token);
    void update(EmailConfirmationToken emailConfirmationToken);
}
