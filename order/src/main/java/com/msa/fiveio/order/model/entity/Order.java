package com.msa.fiveio.order.model.entity;

import com.msa.fiveio.common.auditing.BaseEntity;
import com.msa.fiveio.common.exception.CustomException;
import com.msa.fiveio.common.exception.domain.OrderErrorCode;
import com.msa.fiveio.order.infrastructure.client.dto.response.PromotionDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "p_orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "requester_company_id", nullable = false)
    private UUID requesterCompanyId;

    @Column(name = "receiver_company_id", nullable = false)
    private UUID receiverCompanyId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Long quantity;

    @Column(name = "request_notes", columnDefinition = "TEXT")
    private String requestNotes;

    @Column(
            name = "total_amount",
            nullable = false,
            columnDefinition = "DOUBLE PRECISION DEFAULT 0.0 NOT NULL"
    )
    private Double totalAmount;

    @Builder
    private Order(UUID requesterCompanyId, UUID receiverCompanyId, UUID productId,
                  Double totalAmount, Long quantity, String requestNotes) {
        this.requesterCompanyId = requesterCompanyId;
        this.receiverCompanyId = receiverCompanyId;
        this.productId = productId;
        this.quantity = quantity;
        this.requestNotes = requestNotes;
        this.totalAmount = totalAmount != null ? totalAmount : 0.0;
        ;
    }

    public static Order createOrder(
            UUID requesterCompanyId,
            UUID receiverCompanyId,
            UUID productId,
            Long quantity,
            String requestNotes
    ) {
        return Order.builder()
                .requesterCompanyId(requesterCompanyId)
                .receiverCompanyId(receiverCompanyId)
                .productId(productId)
                .quantity(quantity)
                .requestNotes(requestNotes)
                .build();
    }

    public void calculateTotalAmount(Double productPrice) {
        this.totalAmount = this.quantity * productPrice;
    }

    public void update(Long quantity, String requestNotes) {
        if (quantity != null) {
            validateUpdateQuantity(quantity);
            Double price = totalAmount / this.quantity;
            this.quantity = quantity;
            calculateTotalAmount(price);
        }
        if (requestNotes != null) {
            this.requestNotes = requestNotes;
        }
    }

    public void applyPromotion(PromotionDto promotion) {
        if (promotion == null) return;

        switch (promotion.getDiscountType()) {
            case PERCENT ->
                    this.totalAmount = this.totalAmount * (100 - promotion.getDiscountValue().doubleValue()) / 100;
            case AMOUNT ->
                    this.totalAmount = Math.max(0, this.totalAmount - promotion.getDiscountValue().doubleValue());
            default -> throw new IllegalArgumentException("알 수 없는 할인 타입:" + promotion.getDiscountType());
        }
    }

    private void validateUpdateQuantity(Long quantity) {
        if (quantity < 1) {
            throw new CustomException(OrderErrorCode.INVALID_QUANTITY);
        }
    }
}
