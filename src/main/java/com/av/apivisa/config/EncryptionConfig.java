package com.av.apivisa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
public class EncryptionConfig {

    @Value("${app.encryption.key:}")
    private String encryptionKey;

    @Bean
    public SecretKey secretKey() throws NoSuchAlgorithmException {
        if (encryptionKey != null && !encryptionKey.trim().isEmpty() && isValidBase64(encryptionKey)) {
            try {
                // Use a chave fornecida nas propriedades
                byte[] decodedKey = Base64.getDecoder().decode(encryptionKey);
                return new SecretKeySpec(decodedKey, "AES");
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid encryption key, generating a new key....");
                return generateNewKey();
            }
        } else {
            // Gera uma nova chave (apenas para desenvolvimento)
            System.err.println("Encryption key not provided, generating a new key....");
            return generateNewKey();
        }
    }

    private SecretKey generateNewKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey newKey = keyGen.generateKey();

        // Imprime a nova chave para uso no application.properties
        String base64Key = Base64.getEncoder().encodeToString(newKey.getEncoded());
        System.out.println("=== NEW ENCRYPTION KEY GENERATED ===");
        System.out.println("Add this line to application.properties:");
        System.out.println("app.encryption.key=" + base64Key);
        System.out.println("===========================================");

        return newKey;
    }

    private boolean isValidBase64(String str) {
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
