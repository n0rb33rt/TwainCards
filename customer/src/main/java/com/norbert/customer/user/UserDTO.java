package com.norbert.customer.user;

import com.norbert.customer.deck.Deck;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(String email,Role role, LocalDateTime subscriptionUntil, List<Deck> decks) {
}
