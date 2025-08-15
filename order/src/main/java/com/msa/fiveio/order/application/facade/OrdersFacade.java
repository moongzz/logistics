package com.msa.fiveio.order.application.facade;

import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import com.msa.fiveio.order.application.dto.request.OrderSearchRequestDto;
import com.msa.fiveio.order.application.dto.request.OrderUpdateRequestDto;
import com.msa.fiveio.order.application.dto.response.OrderCreateResponseDto;
import com.msa.fiveio.order.application.dto.response.OrderResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdersFacade {

    OrderCreateResponseDto createOrder(OrderCreateRequestDto orderCreateRequestDto);

    Page<OrderResponseDto> readOrders(OrderSearchRequestDto requestDto, Pageable pageable);

    OrderResponseDto readOrder(UUID orderId);

    OrderResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto requestDto);

    void cancelOrder(UUID orderId, Long userId);

    void deleteOrder(UUID orderId, Long userId);
}
