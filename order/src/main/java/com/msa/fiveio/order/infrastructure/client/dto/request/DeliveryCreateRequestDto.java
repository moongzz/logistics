package com.msa.fiveio.order.infrastructure.client.dto.request;

import com.msa.fiveio.order.infrastructure.client.dto.response.ProductResponseDto;
import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryCreateRequestDto {

    private final UUID orderId;
    private final UUID departHubId;
    private final UUID arriveHubId;
    private final String deliveryAddress;
    private final String recipientName;
    private final String recipientSlackId;
    private final Long quantity;
    private final String requestNotes;
    private final String productName;


    public DeliveryCreateRequestDto(
        UUID orderId,
        ProductResponseDto productInfo,
        OrderCreateRequestDto orderInfo
    ) {
        this.orderId = orderId;
        this.departHubId = productInfo.getDepartHubId();
        this.arriveHubId = productInfo.getArriveHubId();
        this.deliveryAddress = productInfo.getDeliveryAddress();
        this.recipientName = orderInfo.getRecipientName();
        this.recipientSlackId = orderInfo.getRecipientSlackId();
        this.quantity = orderInfo.getQuantity();
        this.requestNotes = orderInfo.getRequestNotes();
        this.productName = productInfo.getProductName();
    }
}
