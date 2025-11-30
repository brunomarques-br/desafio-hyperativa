package com.av.apivisa.service;


import com.av.apivisa.dto.CardRequestTO;
import com.av.apivisa.dto.FileUploadResponseTO;
import com.av.apivisa.entity.BatchStatus;
import com.av.apivisa.entity.FileProcessingBatch;
import com.av.apivisa.entity.User;
import com.av.apivisa.exception.BatchProcessingException;
import com.av.apivisa.repository.FileProcessingBatchRepository;
import com.av.apivisa.util.FileParseResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileProcessingService {

    private final CardService cardService;
    private final FileProcessingBatchRepository batchRepository;
    private final AuthService authService;

    public FileUploadResponseTO processTxtFile(MultipartFile file, boolean validateOnly) {
        List<CardRequestTO> cards;
        String batchNumber = null;

        try {
            // File parse
            FileParseResultUtil parseResult = parseTxtFile(file);
            cards = parseResult.cards();
            batchNumber = parseResult.batchNumber();

        } catch (IOException e) {
            return new FileUploadResponseTO(
                    null,"", 0, 0, 1,
                    List.of("Error reading file: " + e.getMessage()),
                    "ERROR", "File processing failed."
            );
        }

        // Create batch record
        FileProcessingBatch batch = createBatchRecord(batchNumber, cards.size());

        // Process the cards
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        for (int i = 0; i < cards.size(); i++) {
            CardRequestTO cardRequestTO = cards.get(i);
            cardRequestTO.setLineNumber(i + 1);

            try {
                if (!validateOnly) {
                    cardService.saveCard(cardRequestTO);
                }
                successCount++;
            } catch (Exception e) {
                errors.add(String.format("Line %d: %s", i + 1, e.getMessage()));
            }
        }

        // Update batch status
        updateBatchStatus(batch, cards.size(), successCount, errors.size(), validateOnly);

        return createUploadResponse(batch, cards.size(), successCount, errors, validateOnly);
    }

    private FileParseResultUtil parseTxtFile(MultipartFile file) throws IOException {
        List<CardRequestTO> cards = new ArrayList<>();
        String currentBatch = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("DESAFIO-HYPERATIVA")) {
                    // Process batch header
                    currentBatch = parseBatchHeader(line);
                } else if (line.startsWith("C") && line.length() > 7) {
                    // Processes card line
                    CardRequestTO card = parseCardLine(line, currentBatch);
                    if (card != null) {
                        cards.add(card);
                    }
                } else if (line.startsWith("LOTE")) {
                    // Process batch footer (can be used for validation)
                    // For while, we ignore it.
                }
            }
        }

        return new FileParseResultUtil(cards, currentBatch);
    }


    private String parseBatchHeader(String line) {
        try {
            // Formato: [01-29]NAME [30-37]DATA [38-45]BATCH [46-51]NUMBER OF RECORDS
            if (line.length() >= 45) {
                String lote = line.substring(37, 45).trim();
                return lote.isEmpty() ? "BATCH_" + System.currentTimeMillis() : lote;
            }
        } catch (Exception e) {
            System.err.println("Error header parser: " + e.getMessage());
        }
        return "BATCH_" + System.currentTimeMillis();
    }

    private CardRequestTO parseCardLine(String line, String batchNumber) {
        try {
            // [01-01] Identifier
            String lineIdentifier = line.substring(0, 1); // C
            // [02-07] Numbering in batch
            String numberingInBatch = line.substring(1, 7).trim();
            // [08-26] Card Number
            String cardNumber = line.substring(7, Math.min(27, line.length())).trim();

            // Check if the card number is not empty / null.
            if (cardNumber.isEmpty()) {
                return null;
            }

            CardRequestTO cardRequestTO = new CardRequestTO();
            cardRequestTO.setCardNumber(cardNumber);
            cardRequestTO.setBatchNumber(batchNumber != null ? batchNumber : "UNKNOWN");
            cardRequestTO.setLineIdentifier(lineIdentifier);

            cardRequestTO.setNumberingInBatch(numberingInBatch);

            return cardRequestTO;

        } catch (Exception e) {
            return null;
        }
    }

    private FileProcessingBatch createBatchRecord(String batchNumber, int totalRecords) {

        Optional<FileProcessingBatch> verifyBatchExists = batchRepository.findByBatchNumber(batchNumber);
        if (verifyBatchExists.isPresent()) {
            throw new BatchProcessingException("Batch number already exists: " + batchNumber);
        }

        User currentUser = authService.getCurrentUser();
        try {
            FileProcessingBatch batch = new FileProcessingBatch();
            batch.setBatchNumber(batchNumber != null ? batchNumber : "BATCH_" + System.currentTimeMillis());
            batch.setNome("DESAFIO-HYPERATIVA");
            batch.setData(String.valueOf(LocalDateTime.now().getYear()));
            batch.setQuantidadeRegistros(totalRecords);
            batch.setProcessedBy(currentUser);
            batch.setStatus(BatchStatus.PROCESSING);
            return batchRepository.save(batch);
        }catch (Exception e) {
            throw new BatchProcessingException("Error creating batch record: " + e.getMessage());
        }
    }

    private void updateBatchStatus(FileProcessingBatch batch, int totalRecords,
                                   int successCount, int errorCount, boolean validateOnly) {
        batch.setRegistrosProcessados(totalRecords);
        batch.setRegistrosComErro(errorCount);

        if (validateOnly) {
            batch.setStatus(BatchStatus.COMPLETED);
            batch.setErrorDetails("Processing in validation mode only");
        } else if (errorCount == 0) {
            batch.setStatus(BatchStatus.COMPLETED);
        } else if (successCount == 0) {
            batch.setStatus(BatchStatus.FAILED);
        } else {
            batch.setStatus(BatchStatus.PARTIALLY_COMPLETED);
        }

        batch.setProcessedAt(LocalDateTime.now());
        batchRepository.save(batch);
    }

    private FileUploadResponseTO createUploadResponse(FileProcessingBatch batch, int totalRecords,
                                                      int successCount, List<String> errors, boolean validateOnly) {
        String statusMessage = validateOnly ? "Validation completed" : "Processing complete.";
        String mode = validateOnly ? " (Validation)" : "";

        return new FileUploadResponseTO(
                batch.getId(),
                batch.getBatchNumber(),
                totalRecords,
                successCount,
                errors.size(),
                errors,
                batch.getStatus().name(),
                statusMessage + mode + ". Sucessos: " + successCount + ", Erros: " + errors.size()
        );
    }
}