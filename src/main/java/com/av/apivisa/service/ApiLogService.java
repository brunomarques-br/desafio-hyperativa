package com.av.apivisa.service;

import com.av.apivisa.dto.ApiLogResponseTO;
import com.av.apivisa.dto.mapper.ApiLogResponseMapper;
import com.av.apivisa.entity.ApiLog;
import com.av.apivisa.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    public List<ApiLogResponseTO> getAllRecords() {
        List<ApiLog> logs = apiLogRepository.findAllLogsOrderByDate();
        return logs.stream().map(ApiLogResponseMapper::toDto).toList();
    }


}
