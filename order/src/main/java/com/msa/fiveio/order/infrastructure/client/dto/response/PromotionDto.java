package com.msa.fiveio.order.infrastructure.client.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class PromotionDto {
    private final UUID promotionId;
    private final String name;
    private final DiscountType discountType;
    private final BigDecimal discountValue;

    public enum DiscountType {
        PERCENT, AMOUNT
    }
}