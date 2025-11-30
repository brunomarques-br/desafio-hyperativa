package com.av.apivisa.dto.mapper;

import com.av.apivisa.dto.ApiLogResponseTO;
import com.av.apivisa.entity.ApiLog;

public class ApiLogResponseMapper {

    public static ApiLogResponseTO toDto(ApiLog apiLog) {
        ApiLogResponseTO dto = new ApiLogResponseTO();
        dto.setId(apiLog.getId());
        dto.setEndpoint(apiLog.getEndpoint());
        dto.setHttpMethod(apiLog.getHttpMethod());
        dto.setStatusCode(apiLog.getStatusCode());
        dto.setClientIp(apiLog.getClientIp());
        dto.setUserAgent(apiLog.getUserAgent());
        dto.setProcessingTimeMs(apiLog.getProcessingTimeMs());
        dto.setUserId(apiLog.getUserId());
        dto.setTimestamp(apiLog.getTimestamp());
        dto.setErrorCode(apiLog.getErrorCode());
        dto.setResponseBody(apiLog.getResponseBody());
        return dto;
    }

}
