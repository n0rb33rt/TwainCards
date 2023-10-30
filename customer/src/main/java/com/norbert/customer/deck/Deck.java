package com.norbert.customer.deck;

import com.norbert.customer.card.Card;
import com.norbert.customer.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Deck")
@Table(name = "deck")
public class Deck {
    @Id
    @SequenceGenerator(
            name = "deck_id_seq",
            sequenceName = "deck_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "deck_id_seq"
    )
    @Column(
            name = "id",
            updatable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "native_language",
            nullable = false,
            columnDefinition = "VARCHAR(13)"
    )
    @Enumerated(EnumType.STRING)
    private Language nativeLanguage;

    @Column(
            name = "learning_language",
            nullable = false,
            columnDefinition = "VARCHAR(13)"
    )
    @Enumerated(EnumType.STRING)
    private Language learningLanguage;

    @Column(
            name = "total_words",
            nullable = false,
            columnDefinition = "INT"
    )
    private Integer totalWords;

    @Column(
            name = "words_to_learn",
            nullable = false,
            columnDefinition = "INT"
    )
    private Integer wordsToLearn;

    @Column(
            name = "known_words",
            nullable = false,
            columnDefinition = "INT"
    )
    private Integer knownWords;

    @Column(
            name = "learned_words",
            nullable = false,
            columnDefinition = "INT"
    )
    private Integer learnedWords;


    @ManyToOne(
            cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.DETACH},
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_id_fk"),
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT"
    )
    private User user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "deck"
    )
    private List<Card> cards;
}
