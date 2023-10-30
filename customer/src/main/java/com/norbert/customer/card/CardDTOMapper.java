package com.norbert.customer.card;

import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class CardDTOMapper implements Function<Card,CardDTO> {

    @Override
    public CardDTO apply(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .nativeWord(card.getNativeWord())
                .translation(card.getTranslation())
                .description(card.getDescription())
                .build();
    }
}
