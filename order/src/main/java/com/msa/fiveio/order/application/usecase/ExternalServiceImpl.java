package com.msa.fiveio.order.application.usecase;

import com.msa.fiveio.common.exception.CustomException;
import com.msa.fiveio.common.exception.domain.OrderErrorCode;
import com.msa.fiveio.order.infrastructure.client.DeliveryClient;
import com.msa.fiveio.order.infrastructure.client.ProductClient;
import com.msa.fiveio.order.infrastructure.client.dto.request.DeliveryCreateRequestDto;
import com.msa.fiveio.order.infrastructure.client.dto.response.ProductResponseDto;
import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService {

    private final DeliveryClient deliveryClient;
    private final ProductClient productClient;

    @Override
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

    @Override
    public ProductResponseDto sendProductRequest(OrderCreateRequestDto orderInfo) {
        try {
            return productClient.processOrderRequest(orderInfo.getProductId(),
                orderInfo.getReceiverCompanyId(), orderInfo.getQuantity());
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.PRODUCT_REQUEST_FAILED);
        }
    }

    @Override
    public String getDeliveryStatus(UUID orderId) {
        try {
            return deliveryClient.getDeliveryStatus(orderId);
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.DELIVERY_STATUS_FETCH_FAILED);
        }
    }

    @Override
    public void rollbackStock(UUID productId, Long quantity) {
        try {
            productClient.rollbackStock(productId, quantity);
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.STOCK_ROLLBACK_FAILED);
        }
    }

    @Override
    public void cancelDelivery(UUID orderId, Long userId) {
        try {
            deliveryClient.cancelDelivery(orderId, userId);
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.DELIVERY_DELETE_FAILED);
        }
    }
}
