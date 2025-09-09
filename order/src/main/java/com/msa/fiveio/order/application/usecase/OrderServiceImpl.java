package com.msa.fiveio.order.application.usecase;

import com.msa.fiveio.common.exception.CustomException;
import com.msa.fiveio.common.exception.domain.OrderErrorCode;
import com.msa.fiveio.order.infrastructure.client.dto.response.ProductResponseDto;
import com.msa.fiveio.order.model.repository.OrderRepository;
import com.msa.fiveio.order.application.dto.request.OrderSearchRequestDto;
import com.msa.fiveio.order.application.dto.request.OrderUpdateRequestDto;
import com.msa.fiveio.order.application.dto.response.OrderResponseDto;
import com.msa.fiveio.order.presentation.mapper.OrderMapper;
import com.msa.fiveio.order.model.entity.Order;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(
        readOnly = true
)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public Order createOrder(ProductResponseDto productInfo, Order order) {
        order.calculateTotalAmount(productInfo.getProductPrice());
        return orderRepository.save(order);
    }

    @Override
    public Page<OrderResponseDto> readOrders(OrderSearchRequestDto requestDto, Pageable pageable) {
        Page<Order> orderPage = orderRepository.readOrders(requestDto, pageable);
        return orderPage.map(OrderMapper::OrderToOrderResponseDto);
    }

    @Transactional
    @Override
    public void updateOrder(Order order, OrderUpdateRequestDto requestDto) {
        order.update(requestDto.getQuantity(), requestDto.getRequestNotes());
    }

    @Override
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public void cancelOrder(Order order, Long userId, String status) {
        validateOrderStatus(status, "HUB_PENDING");
        orderDelete(order, userId);
        log.info("Order {} cancelled by user: {}", order.getOrderId(), userId);
    }

    @Transactional
    @Override
    public void deleteOrder(Order order, Long userId, String status) {
        validateOrderStatus(status, "DELIVERED");
        orderDelete(order, userId);
        log.info("Order {} deleted by user: {}", order.getOrderId(), userId);
    }

    private void validateOrderStatus(String Status, String expectedStatus) {
        if (!Status.equals(expectedStatus)) {
            throw new CustomException(OrderErrorCode.INVALID_ORDER_STATUS);
        }
    }

    private void orderDelete(Order order, Long userId) {
        order.addDeletedField(userId);
    }
}
