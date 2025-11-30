package com.av.apivisa.util;

import com.av.apivisa.dto.CardRequestTO;

import java.util.List;

public record FileParseResultUtil(List<CardRequestTO> cards, String batchNumber) { }
