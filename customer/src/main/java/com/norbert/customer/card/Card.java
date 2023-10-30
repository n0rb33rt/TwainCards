package com.norbert.customer.card;

import com.norbert.customer.deck.Deck;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Card")
@Table(name = "card")
@ToString
public class Card {
    @Id
    @SequenceGenerator(
            name = "card_id_seq",
            sequenceName = "card_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "card_id_seq"
    )
    @Column(
            name = "id",
            updatable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "native_word",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String nativeWord;

    @Column(
            name = "translation",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String translation;

    @Column(
            name = "description",
            columnDefinition = "VARCHAR(150)"
    )
    private String description;

    @Column(
            name = "state",
            nullable = false,
            columnDefinition = "VARCHAR(13)"
    )
    @Enumerated(EnumType.STRING)
    private State state;




    @ManyToOne
    @JoinColumn(
            name = "deck_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "deck_id_fk"),
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT"
    )
    private Deck deck;
}
