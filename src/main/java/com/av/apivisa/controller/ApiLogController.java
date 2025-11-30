package com.av.apivisa.controller;

import com.av.apivisa.dto.ApiLogResponseTO;
import com.av.apivisa.repository.ApiLogRepository;
import com.av.apivisa.service.ApiLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class ApiLogController {

    private final ApiLogService apiLogService;

    @GetMapping("/all")
    public ResponseEntity<List<ApiLogResponseTO>> getAllRecords() {
        return ResponseEntity.ok().body(apiLogService.getAllRecords());
    }

}
