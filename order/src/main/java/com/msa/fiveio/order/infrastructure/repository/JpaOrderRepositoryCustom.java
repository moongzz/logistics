package com.msa.fiveio.order.infrastructure.repository;

import com.msa.fiveio.order.model.entity.Order;
import com.msa.fiveio.order.application.dto.request.OrderSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JpaOrderRepositoryCustom {
    Page<Order> readOrders(OrderSearchRequestDto requestDto, Pageable pageable);
}
