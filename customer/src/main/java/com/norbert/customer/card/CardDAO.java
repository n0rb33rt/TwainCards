package com.norbert.customer.card;

import java.util.List;
import java.util.Optional;

public interface CardDAO {
    void save(Long deckId,CardDTO card);
    List<Card> getCardsByDeckId(Long id);

    void removeCard(Long id);
    Optional<Card> findCardById(Long id);
 }
