package com.norbert.customer.deck;

public interface DeckDAO {
    void save(Deck deck);
    void remove(Long id);
}
