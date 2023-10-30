package com.norbert.customer.deck;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeckDTO {
    @JsonProperty("id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @JsonProperty("native_language")
    private Language nativeLanguage;

    @Enumerated(EnumType.STRING)
    @JsonProperty("learning_language")
    private Language learningLanguage;

    @JsonProperty("total_words")
    private Integer totalWords;

    @JsonProperty("words_to_learn")
    private Integer wordsToLearn;

    @JsonProperty("known_words")
    private Integer knownWords;

    @JsonProperty("learned_words")

    private Integer learnedWords;
}
