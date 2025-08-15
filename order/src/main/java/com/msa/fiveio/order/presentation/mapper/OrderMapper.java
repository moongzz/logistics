package com.msa.fiveio.order.presentation.mapper;

import com.msa.fiveio.order.model.entity.Order;
import com.msa.fiveio.order.application.dto.response.OrderCreateResponseDto;
import com.msa.fiveio.order.application.dto.response.OrderResponseDto;

public class OrderMapper {

    public static OrderCreateResponseDto orderIdToOrderCreateResponseDto(Order order) {
        return OrderCreateResponseDto.builder()
            .orderId(order.getOrderId())
            .build();
    }

    public static OrderResponseDto OrderToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
            .orderId(order.getOrderId())
            .requesterCompanyId(order.getRequesterCompanyId())
            .receiverCompanyId(order.getReceiverCompanyId())
            .productId(order.getProductId())
            .quantity(order.getQuantity())
            .requestNotes(order.getRequestNotes())
            .build();
    }
}
