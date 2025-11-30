package com.av.apivisa.controller;

import com.av.apivisa.dto.FileUploadResponseTO;
import com.av.apivisa.service.FileProcessingService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileProcessingService fileProcessingService;

    public FileUploadController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseTO> uploadFile(
            @Parameter(
                    description = "Arquivo TXT no formato Hyperativa",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "validateOnly", defaultValue = "false") boolean validateOnly) {
        FileUploadResponseTO response = fileProcessingService.processTxtFile(file, validateOnly);
        return ResponseEntity.ok(response);
    }
}