package com.msa.fiveio.promotion.application.dto.request;

import com.msa.fiveio.promotion.model.entity.Promotion;
import com.msa.fiveio.promotion.model.vo.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class ReqPromotionPostDto {
    private final UUID productId;
    private final String name;
    private final DiscountType discountType;
    private final BigDecimal discountValue;

    public Promotion create() {
        return Promotion.createPromotion(productId, name, discountType, discountValue);
    }
}
