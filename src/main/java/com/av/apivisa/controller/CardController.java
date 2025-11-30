package com.av.apivisa.controller;

import com.av.apivisa.dto.CardRequestTO;
import com.av.apivisa.dto.CardResponseTO;
import com.av.apivisa.dto.CardSearchResponseTO;
import com.av.apivisa.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/single")
    public ResponseEntity<CardResponseTO> addCard(@Valid @RequestBody CardRequestTO request) {
        CardResponseTO response = cardService.saveCard(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<CardSearchResponseTO> searchCard(
            @RequestParam String cardNumber) {
        CardSearchResponseTO response = cardService.findByCardNumber(cardNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> cardExists(@RequestParam String cardNumber) {
        boolean exists = cardService.cardExists(cardNumber);
        return ResponseEntity.ok(exists);
    }
}