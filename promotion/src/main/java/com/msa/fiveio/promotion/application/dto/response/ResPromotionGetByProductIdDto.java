package com.msa.fiveio.promotion.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.msa.fiveio.promotion.model.entity.Promotion;
import com.msa.fiveio.promotion.model.vo.DiscountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public class ResPromotionGetByProductIdDto {
    @JsonProperty
    private UUID promotionId;
    @JsonProperty
    private String name;
    @JsonProperty
    private DiscountType discountType;
    @JsonProperty
    private BigDecimal discountValue;

    public static ResPromotionGetByProductIdDto of(Promotion promotion) {
        return ResPromotionGetByProductIdDto.builder()
                .promotionId(promotion.getPromotionId())
                .name(promotion.getName())
                .discountType(promotion.getDiscount().getDiscountType())
                .discountValue(promotion.getDiscount().getDiscountValue())
                .build();
    }
}
