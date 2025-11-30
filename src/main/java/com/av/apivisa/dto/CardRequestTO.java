package com.av.apivisa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardRequestTO {

    @NotBlank(message = "Card Number is required")
    @Size(min = 13, max = 26, message = "The card number must be between 13 and 26 characters")
    @Pattern(regexp = "^[0-9]+$", message = "The card number must contain only numeric characters")
    @Schema(description = "Card Number", example = "4111111111111111")
    private String cardNumber;

    @NotBlank(message = "Batch Number is required")
    @Size(max = 8, message = "Batch Number must have a maximum of 8 characters")
    @Schema(description = "Batch Number of the card", example = "LOTE0001")
    private String batchNumber;

    @Size(max = 2, message = "Line Identifier must have a maximum of 2 characters")
    @Schema(description = "Line Identifier of the card", example = "C")
    private String lineIdentifier;

    @Size(max = 6, message = "Numbering In Batch must have a maximum of 6 characters")
    @Schema(description = "Numbering In Batch of the card", example = "1")
    private String numberingInBatch;

    @Schema(description = "Line Number of the card", example = "1")
    private Integer lineNumber;
}