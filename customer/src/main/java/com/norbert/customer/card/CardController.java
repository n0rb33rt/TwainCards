package com.norbert.customer.card;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card")
@AllArgsConstructor
public class CardController {
    private final CardService cardService;
    @PostMapping
    public ResponseEntity<Void> saveCard(@RequestParam("deck_id") Long deckId,@RequestBody CardDTO card){
        cardService.save(deckId,card);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<CardDTO>> getCardsByDeckId(@RequestParam("deck_id") Long id){
        return ResponseEntity.ok(cardService.getCardsByDeckId(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCard(@PathVariable("id") Long id){
        cardService.removeCard(id);
        return ResponseEntity.ok().build();
    }
}
