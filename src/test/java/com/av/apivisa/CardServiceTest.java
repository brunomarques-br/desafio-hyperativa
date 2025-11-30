package com.av.apivisa;

import com.av.apivisa.dto.CardRequestTO;
import com.av.apivisa.dto.CardResponseTO;
import com.av.apivisa.dto.CardSearchResponseTO;
import com.av.apivisa.entity.Card;
import com.av.apivisa.entity.User;
import com.av.apivisa.entity.UserRole;
import com.av.apivisa.repository.CardRepository;
import com.av.apivisa.service.AuthService;
import com.av.apivisa.service.CardService;
import com.av.apivisa.service.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private CardService cardService;

    private User testUser;
    private CardRequestTO validCardRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID().toString());
        testUser.setEmail("test@hyperativa.com");
        testUser.setName("Test User");
        testUser.setRole(UserRole.ADMIN);

        validCardRequest = new CardRequestTO();
        validCardRequest.setCardNumber("4111111111111111");
        validCardRequest.setBatchNumber("LOTE001");
        validCardRequest.setLineIdentifier("C1");
        validCardRequest.setNumberingInBatch("000001");
    }

    @Test
    void shouldSaveCardSuccessfully() {

        when(authService.getCurrentUser()).thenReturn(testUser);
        when(encryptionService.validateCardNumber(anyString())).thenReturn(true);
        when(encryptionService.generateHash(anyString())).thenReturn("card_hash");
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted_card");
        when(cardRepository.existsByCardHash(anyString())).thenReturn(false);

        Card savedCard = new Card();
        savedCard.setId(UUID.randomUUID().toString());
        savedCard.setBatchNumber("LOTE001");
        savedCard.setLineIdentifier("C1");
        savedCard.setNumberingInBatch("000001");
        savedCard.setCreatedAt(LocalDateTime.now());
        when(cardRepository.save(any(Card.class))).thenReturn(savedCard);


        CardResponseTO response = cardService.saveCard(validCardRequest);


        assertNotNull(response);
        assertEquals(savedCard.getId(), response.getId());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void shouldThrowExceptionWhenCardAlreadyExists() {

        when(encryptionService.validateCardNumber(anyString())).thenReturn(true);
        when(encryptionService.generateHash(anyString())).thenReturn("card_hash");
        when(cardRepository.existsByCardHash(anyString())).thenReturn(true);


        assertThrows(RuntimeException.class, () -> {
            cardService.saveCard(validCardRequest);
        });

        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void shouldFindCardByNumber() {
        // Arrange
        String cardNumber = "4111111111111111";
        String cardHash = "card_hash";

        when(encryptionService.validateCardNumber(cardNumber)).thenReturn(true);
        when(encryptionService.generateHash(cardNumber)).thenReturn(cardHash);

        Card card = new Card();
        card.setId(UUID.randomUUID().toString());
        card.setBatchNumber("LOTE001");
        card.setCreatedAt(LocalDateTime.now());

        when(cardRepository.findByCardHash(cardHash)).thenReturn(Optional.of(card));

        CardSearchResponseTO response = cardService.findByCardNumber(cardNumber);

        assertNotNull(response);
        assertTrue(response.isExists());
        assertEquals(card.getId(), response.getCardId());
    }

    @Test
    void shouldReturnNotFoundForNonExistentCard() {

        String cardNumber = "4111111111111111";

        when(encryptionService.validateCardNumber(cardNumber)).thenReturn(true);
        when(encryptionService.generateHash(cardNumber)).thenReturn("card_hash");
        when(cardRepository.findByCardHash(anyString())).thenReturn(Optional.empty());

        CardSearchResponseTO response = cardService.findByCardNumber(cardNumber);

        assertNotNull(response);
        assertFalse(response.isExists());
        assertNull(response.getCardId());
    }

    @Test
    void shouldCheckCardExists() {
        String cardNumber = "4111111111111111";

        when(encryptionService.validateCardNumber(cardNumber)).thenReturn(true);
        when(encryptionService.generateHash(cardNumber)).thenReturn("card_hash");
        when(cardRepository.existsByCardHash("card_hash")).thenReturn(true);

        boolean exists = cardService.cardExists(cardNumber);

        assertTrue(exists);
    }
}
