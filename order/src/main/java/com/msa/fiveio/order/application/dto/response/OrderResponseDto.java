package com.msa.fiveio.order.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    @JsonProperty("order-id")
    private UUID orderId;
    @JsonProperty("requester-company-id")
    private UUID requesterCompanyId;
    @JsonProperty("receiver-company-id")
    private UUID receiverCompanyId;
    @JsonProperty("product-id")
    private UUID productId;
    @JsonProperty("quantity")
    private Long quantity;
    @JsonProperty("delivery-id")
    private UUID deliveryId;
    @JsonProperty("request-notes")
    private String requestNotes;

}
