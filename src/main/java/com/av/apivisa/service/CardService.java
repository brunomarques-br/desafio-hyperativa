package com.av.apivisa.service;

import com.av.apivisa.dto.CardRequestTO;
import com.av.apivisa.dto.CardResponseTO;
import com.av.apivisa.dto.CardSearchResponseTO;
import com.av.apivisa.entity.Card;
import com.av.apivisa.entity.User;
import com.av.apivisa.repository.CardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final EncryptionService encryptionService;
    private final AuthService authService;

    @Transactional
    public CardResponseTO saveCard(CardRequestTO request) {
        // Validating card number format
        if (!encryptionService.validateCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Número do cartão inválido");
        }

        // Generates a hash for duplicate verification.
        String cardHash = encryptionService.generateHash(request.getCardNumber());

        // Check if the card already exists.
        if (cardRepository.existsByCardHash(cardHash)) {
            throw new IllegalArgumentException("Cartão já cadastrado no sistema");
        }

        // Encrypts the card number.
        String encryptedCard = encryptionService.encrypt(request.getCardNumber());

        // Get current user
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("Unauthenticated user");
        }

        // Create and save the card entity
        Card card = new Card();
        card.setEncryptedCardNumber(encryptedCard);
        card.setCardHash(cardHash);
        card.setBatchNumber(request.getBatchNumber());
        card.setLineIdentifier(request.getLineIdentifier());
        card.setNumberingInBatch(request.getNumberingInBatch());
        card.setLineNumber(request.getLineNumber());
        card.setCreatedBy(currentUser);

        Card savedCard = cardRepository.save(card);
        return mapToResponse(savedCard);
    }

    public CardSearchResponseTO findByCardNumber(String cardNumber) {
        // Check if the card number format is valid
        if (!encryptionService.validateCardNumber(cardNumber)) {
            return CardSearchResponseTO.notFound();
        }

        // Generates hash for search.
        String cardHash = encryptionService.generateHash(cardNumber);

        // Search for the card by its hash.
        Optional<Card> cardOpt = cardRepository.findByCardHash(cardHash);

        if (cardOpt.isPresent()) {
            Card card = cardOpt.get();
            return CardSearchResponseTO.found(
                    card.getId(),
                    card.getBatchNumber(),
                    card.getCreatedAt()
            );
        } else {
            return CardSearchResponseTO.notFound();
        }
    }

    public boolean cardExists(String cardNumber) {
        if (!encryptionService.validateCardNumber(cardNumber)) {
            return false;
        }

        String cardHash = encryptionService.generateHash(cardNumber);
        return cardRepository.existsByCardHash(cardHash);
    }

    private CardResponseTO mapToResponse(Card card) {
        return new CardResponseTO(
                card.getId(),
                card.getBatchNumber(),
                card.getLineIdentifier(),
                card.getNumberingInBatch(),
                card.getCreatedAt()
        );
    }
}