package com.av.apivisa;

import com.av.apivisa.service.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        ReflectionTestUtils.setField(encryptionService, "encryptionKey",
                "4/IMriguioAAJ8fMsYTNsXpQRU6Fg9kLztQIdJnffiE=");
    }

    @Test
    void shouldEncryptAndDecryptSuccessfully() {
        String originalText = "4111111111111111";

        String encrypted = encryptionService.encrypt(originalText);
        String decrypted = encryptionService.decrypt(encrypted);

        assertNotNull(encrypted);
        assertNotEquals(originalText, encrypted);
        assertEquals(originalText, decrypted);
    }

    @Test
    void shouldGenerateConsistentHash() {
        String cardNumber = "4111111111111111";

        String hash1 = encryptionService.generateHash(cardNumber);
        String hash2 = encryptionService.generateHash(cardNumber);

        assertNotNull(hash1);
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldValidateCardNumberCorrectly() {
        assertTrue(encryptionService.validateCardNumber("4111111111111111"));
        assertTrue(encryptionService.validateCardNumber("1234567890123456"));

        assertFalse(encryptionService.validateCardNumber("4111-1111-1111-1111"));
        assertFalse(encryptionService.validateCardNumber(""));
        assertFalse(encryptionService.validateCardNumber(null));
        assertFalse(encryptionService.validateCardNumber("123456789012")); // too short
        assertFalse(encryptionService.validateCardNumber("123456789012345678901234567")); // too long
    }

    @Test
    void shouldThrowExceptionWhenEncryptionFails() {
        ReflectionTestUtils.setField(encryptionService, "encryptionKey", "invalid-key");

        assertThrows(RuntimeException.class, () -> {
            encryptionService.encrypt("4111111111111111");
        });
    }
}