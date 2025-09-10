package com.msa.fiveio.promotion.application.service;

import com.msa.fiveio.promotion.application.dto.request.ReqPromotionPostDto;
import com.msa.fiveio.promotion.application.dto.response.ResPromotionGetByProductIdDto;

import java.util.UUID;

public interface PromotionService {
    void postBy(ReqPromotionPostDto dto);
    ResPromotionGetByProductIdDto getBy(UUID productId);
}
