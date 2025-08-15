package com.msa.fiveio.order.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.msa.fiveio.order.model.entity.Order;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateRequestDto {

    @JsonProperty("receiver-company-id")
    private final UUID receiverCompanyId;

    @JsonProperty("product-id")
    private final UUID productId;

    @JsonProperty("quantity")
    private final Long quantity;

    @JsonProperty("request-notes")
    private final String requestNotes;

    @JsonProperty("recipient-name")
    private final String recipientName;

    @JsonProperty("recipient-slack-id")
    private final String recipientSlackId;

    public Order createOrder(UUID requesterCompanyId) {
        return Order.builder()
            .requesterCompanyId(requesterCompanyId)
            .receiverCompanyId(receiverCompanyId)
            .productId(productId)
            .quantity(quantity)
            .requestNotes(requestNotes)
            .build();
    }
}
