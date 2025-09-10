package com.msa.fiveio.promotion.model.entity;

import com.msa.fiveio.common.auditing.BaseEntity;
import com.msa.fiveio.promotion.model.vo.DiscountType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "p_promotions")
public class Promotion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "promotion_id", nullable = false)
    private UUID promotionId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Discount discount;

    @Builder
    private Promotion(
            UUID productId,
            String name,
            Discount discount
    ) {
        this.productId = productId;
        this.name = name;
        this.discount = discount;
    }

    public static Promotion createPromotion(
            UUID productId,
            String name,
            DiscountType discountType,
            BigDecimal discountValue
    ) {
        return Promotion.builder()
                .productId(productId)
                .name(name)
                .discount(new Discount(discountType, discountValue))
                .build();
    }
}
