package com.msa.fiveio.order.application.usecase;

import com.msa.fiveio.order.infrastructure.client.PromotionClient;
import com.msa.fiveio.order.infrastructure.client.dto.response.PromotionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionClient promotionClient;

    public PromotionDto getPromotion(UUID productId) {
        try {
            return promotionClient.getByProductId(productId);
        } catch (Exception e) {
            return null;
        }
    }
}
