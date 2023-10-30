package com.norbert.customer.deck;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deck")
@AllArgsConstructor
public class DeckController {
    private final DeckService deckService;

    @PostMapping
    public ResponseEntity<Void> saveDeck(@RequestBody DeckDTO deckDTO){
        deckService.save(deckDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDeck(@PathVariable("id")Long id){
        deckService.remove(id);
        return ResponseEntity.ok().build();
    }
}
