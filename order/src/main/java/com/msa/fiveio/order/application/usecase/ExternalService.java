package com.msa.fiveio.order.application.usecase;

import com.msa.fiveio.order.infrastructure.client.dto.response.ProductResponseDto;
import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import com.msa.fiveio.order.infrastructure.client.dto.response.PromotionDto;

import java.util.UUID;

public interface ExternalService {

    void sendDeliveryRequest(UUID orderId, ProductResponseDto companyInfo,
        OrderCreateRequestDto orderInfo);

    ProductResponseDto sendProductRequest(OrderCreateRequestDto orderInfo);

    String getDeliveryStatus(UUID orderId);

    void rollbackStock(UUID productId, Long quantity);

    void cancelDelivery(UUID orderId, Long userId);

    PromotionDto getPromotion(UUID productId);
}
