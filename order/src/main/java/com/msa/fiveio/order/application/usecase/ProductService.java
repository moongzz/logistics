package com.msa.fiveio.order.application.usecase;

import com.msa.fiveio.common.exception.CustomException;
import com.msa.fiveio.common.exception.domain.OrderErrorCode;
import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import com.msa.fiveio.order.infrastructure.client.ProductClient;
import com.msa.fiveio.order.infrastructure.client.dto.response.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductClient productClient;

    public ProductResponseDto sendProductRequest(OrderCreateRequestDto orderInfo) {
        try {
            return productClient.processOrderRequest(orderInfo.getProductId(),
                    orderInfo.getReceiverCompanyId(), orderInfo.getQuantity());
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.PRODUCT_REQUEST_FAILED);
        }
    }

    public void rollbackStock(UUID productId, Long quantity) {
        try {
            productClient.rollbackStock(productId, quantity);
        } catch (Exception e) {
            throw new CustomException(OrderErrorCode.STOCK_ROLLBACK_FAILED);
        }
    }
}
