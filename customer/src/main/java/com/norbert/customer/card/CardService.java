package com.norbert.customer.card;


import com.norbert.customer.deck.Deck;
import com.norbert.customer.exception.BadRequestException;
import com.norbert.customer.exception.CardNotFoundException;
import com.norbert.customer.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardService {
    private final CardDAO cardDAO;
    private final CardDTOMapper cardDTOMapper;

    public void save(Long deckId,CardDTO cardDTO){
        verifyPermissionsForUser(deckId);
        cardDAO.save(deckId,cardDTO);
    }
    public List<CardDTO> getCardsByDeckId(Long deckId){
        verifyPermissionsForUser(deckId);
        return cardDAO.getCardsByDeckId(deckId).stream()
                .map(cardDTOMapper).collect(Collectors.toList());
    }

    public void removeCard(Long id){
        Card card = cardDAO.findCardById(id).orElseThrow(() ->
                new CardNotFoundException("Card with id" + id + " is not found"));
        Long deckId = card.getDeck().getId();
        verifyPermissionsForUser(deckId);
        cardDAO.removeCard(id);
    }

    private void verifyPermissionsForUser(Long deckId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Long> deckIds = user.getDecks().stream().map((Deck::getId)).toList();
        if(!deckIds.contains(deckId))
            throw new BadRequestException("Bad params");
    }
}
