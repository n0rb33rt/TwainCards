package com.norbert.customer.deck;

import com.norbert.customer.exception.BadRequestException;
import com.norbert.customer.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DeckService {
    private final DeckDAO deckDAO;

    public void save(DeckDTO deckDTO){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.getDecks().forEach(existingDeck -> {
            if(existingDeck.getNativeLanguage() == deckDTO.getNativeLanguage() &&
                existingDeck.getLearningLanguage() == deckDTO.getLearningLanguage()){
                throw new BadRequestException("Such deck is already existing");
            }
        });
        Deck deck = Deck.builder()
                .cards(new ArrayList<>())
                .learningLanguage(deckDTO.getLearningLanguage())
                .nativeLanguage(deckDTO.getNativeLanguage())
                .learnedWords(0)
                .totalWords(0)
                .knownWords(0)
                .wordsToLearn(0)
                .user(user)
                .build();
        deckDAO.save(deck);
    }

    public void remove(Long id){
        verifyPermissionsForUser(id);
        deckDAO.remove(id);
    }

    private void verifyPermissionsForUser(Long deckId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Long> deckIds = user.getDecks().stream().map((Deck::getId)).toList();
        if(!deckIds.contains(deckId))
            throw new BadRequestException("Bad params");
    }
}
