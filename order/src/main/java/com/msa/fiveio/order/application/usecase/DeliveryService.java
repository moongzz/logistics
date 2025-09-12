package com.msa.fiveio.order.application.usecase;

import com.msa.fiveio.common.exception.CustomException;
import com.msa.fiveio.common.exception.domain.OrderErrorCode;
import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import com.msa.fiveio.order.infrastructure.client.DeliveryClient;
import com.msa.fiveio.order.infrastructure.client.dto.request.DeliveryCreateRequestDto;
import com.msa.fiveio.order.infrastructure.client.dto.response.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryClient deliveryClient;

    public void sendDeliveryRequest(UUID orderId, ProductResponseDto productInfo,
                                    OrderCreateRequestDto orderInfo) {
        try {
            DeliveryCreateRequestDto request = new DeliveryCreateRequestDto(orderId, productInfo,
                    orderInfo);
            deliveryClient.createDelivery(request);
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.DELIVERY_REQUEST_FAILED);
        }
    }

    public String getDeliveryStatus(UUID orderId) {
        try {
            return deliveryClient.getDeliveryStatus(orderId);
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.DELIVERY_STATUS_FETCH_FAILED);
        }
    }

    public void cancelDelivery(UUID orderId, Long userId) {
        try {
            deliveryClient.cancelDelivery(orderId, userId);
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.DELIVERY_DELETE_FAILED);
        }
    }
}
