package com.msa.fiveio.order.application.facade;

import com.msa.fiveio.order.application.usecase.ExternalService;
import com.msa.fiveio.order.application.usecase.OrderService;
import com.msa.fiveio.order.infrastructure.client.dto.response.ProductResponseDto;
import com.msa.fiveio.order.infrastructure.client.dto.response.PromotionDto;
import com.msa.fiveio.order.model.entity.Order;
import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import com.msa.fiveio.order.application.dto.request.OrderSearchRequestDto;
import com.msa.fiveio.order.application.dto.request.OrderUpdateRequestDto;
import com.msa.fiveio.order.application.dto.response.OrderCreateResponseDto;
import com.msa.fiveio.order.application.dto.response.OrderResponseDto;
import com.msa.fiveio.order.presentation.mapper.OrderMapper;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrdersFacade {

    private final OrderService orderService;
    private final ExternalService externalService;

    @Override
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto orderCreateRequestDto) {
        ProductResponseDto productResponseDto = externalService.sendProductRequest(orderCreateRequestDto);
        Order order = orderCreateRequestDto.createOrder(productResponseDto.getRequesterCompanyId());

        PromotionDto promotion = externalService.getPromotion(orderCreateRequestDto.getProductId());
        Order savedOrder = orderService.createOrder(productResponseDto, order);
        orderService.applyPromotion(order, promotion);

        externalService.sendDeliveryRequest(savedOrder.getOrderId(), productResponseDto, orderCreateRequestDto);
        return OrderMapper.orderIdToOrderCreateResponseDto(savedOrder);
    }

    @Override
    public Page<OrderResponseDto> readOrders(OrderSearchRequestDto requestDto, Pageable pageable) {
        return orderService.readOrders(requestDto, pageable);
    }

    @Override
    public OrderResponseDto readOrder(UUID orderId) {
        Order order = orderService.getOrder(orderId);
        return OrderMapper.OrderToOrderResponseDto(order);
    }

    @Override
    public OrderResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto requestDto) {
        Order order = orderService.getOrder(orderId);
        // 개수가 수정될 경우 재고 관리 요청 추가
        orderService.updateOrder(order, requestDto);
        return OrderMapper.OrderToOrderResponseDto(order);
    }

    @Transactional
    @Override
    public void cancelOrder(UUID orderId, Long userId) {
        String status = externalService.getDeliveryStatus(orderId);
        Order order = orderService.getOrder(orderId);
        externalService.rollbackStock(order.getProductId(), order.getQuantity());
        externalService.cancelDelivery(orderId, userId);
        orderService.cancelOrder(order, userId, status);
    }

    @Override
    public void deleteOrder(UUID orderId, Long userId) {
        String status = externalService.getDeliveryStatus(orderId);
        Order order = orderService.getOrder(orderId);
        orderService.deleteOrder(order, userId, status);
    }

}