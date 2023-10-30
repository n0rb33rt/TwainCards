package com.norbert.customer.user;

import com.norbert.customer.deck.Deck;
import com.norbert.customer.jwt.JwtToken;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Entity(name = "User")
@Table(
        name = "\"user\"",
        uniqueConstraints = @UniqueConstraint(name = "unique_email", columnNames = "email")
)
public class User {
    @Id
    @SequenceGenerator(
            name = "user_id_seq",
            sequenceName = "user_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_seq"
    )
    @Column(
            name = "id",
            updatable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "email",
            length = 319,
            nullable = false,
            columnDefinition = "VARCHAR(319)"
    )
    private String email;

    @Column(
            name = "password",
            columnDefinition = "VARCHAR(61)"
    )
    private String password;

    @Column(
            name = "role",
            nullable = false,
            columnDefinition = "VARCHAR(8)"
    )
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(
            name = "enabled",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private Boolean enabled;

    @Column(
            name = "set_password",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private Boolean setPassword;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            mappedBy = "user",
            fetch = FetchType.EAGER
    )
    private List<Deck> decks;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            mappedBy = "user"
    )
    private List<JwtToken> jwtTokens;

    @Column(
            name = "subscription_until",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime subscriptionUntil;

    @Column(
            name = "created_at",
            updatable = false,
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime createdAt;
}
