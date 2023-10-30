package com.norbert.notification.email_confirmation_token;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfirmationToken {
    private Long id;

    private String token;

    private Boolean confirmed;
}
