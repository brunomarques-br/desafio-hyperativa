package com.av.apivisa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CardSearchResponseTO {

    private boolean exists;
    private String cardId;
    private String batchNumber;
    private LocalDateTime createdAt;

    public static CardSearchResponseTO notFound() {
        return new CardSearchResponseTO(false, null, null, null);
    }

    public static CardSearchResponseTO found(String cardId, String batchNumber, LocalDateTime createdAt) {
        return new CardSearchResponseTO(true, cardId, batchNumber, createdAt);
    }
}