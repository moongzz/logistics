package com.msa.fiveio.order.presentation.controller;

import static com.msa.fiveio.common.annotation.ApiPermission.Role.ROLE_COMPANY_MANAGER;
import static com.msa.fiveio.common.annotation.ApiPermission.Role.ROLE_HUB_MANAGER;
import static com.msa.fiveio.common.annotation.ApiPermission.Role.ROLE_MASTER;
import static com.msa.fiveio.common.annotation.ApiPermission.Role.ROLE_DELIVERY_MANAGER;

import com.msa.fiveio.common.annotation.ApiPermission;
import com.msa.fiveio.order.application.facade.OrdersFacade;
import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import com.msa.fiveio.order.application.dto.request.OrderSearchRequestDto;
import com.msa.fiveio.order.application.dto.request.OrderUpdateRequestDto;
import com.msa.fiveio.order.application.dto.response.OrderCreateResponseDto;
import com.msa.fiveio.order.application.dto.response.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Order Service", description = "주문 서비스 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersFacade ordersFacade;

    @ApiPermission(roles = {ROLE_MASTER, ROLE_HUB_MANAGER, ROLE_DELIVERY_MANAGER, ROLE_COMPANY_MANAGER})
    @Operation(summary = "Order 생성", description = "Order 생성 api 입니다.")
    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createOrder(
        @RequestBody OrderCreateRequestDto orderRequestDto) {
        return ResponseEntity.ok(ordersFacade.createOrder(orderRequestDto));
    }

    @ApiPermission(roles = {ROLE_MASTER, ROLE_HUB_MANAGER, ROLE_DELIVERY_MANAGER, ROLE_COMPANY_MANAGER})
    @Operation(summary = "Order 검색", description = "Order 검색 api 입니다.")
    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> readOrders(
        @RequestBody OrderSearchRequestDto requestDto, Pageable pageable
    ) {
        return ResponseEntity.ok(ordersFacade.readOrders(requestDto, pageable));
    }

    @ApiPermission(roles = {ROLE_MASTER, ROLE_HUB_MANAGER, ROLE_DELIVERY_MANAGER, ROLE_COMPANY_MANAGER})
    @Operation(summary = "Order 단건 조회", description = "Order 단건 조회 api 입니다.")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> readOrder(
        @PathVariable("id") UUID orderId,
        @RequestHeader("X-User-Role") String role
    ) {
        log.info("Order Controller Read Role: " + role);
        return ResponseEntity.ok(ordersFacade.readOrder(orderId));
    }

    @ApiPermission(roles = {ROLE_MASTER, ROLE_HUB_MANAGER})
    @Operation(summary = "Order 수정", description = "Order 수정 api 입니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(
        @PathVariable("id") UUID orderId,
        @RequestBody OrderUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(ordersFacade.updateOrder(orderId, requestDto));
    }

    @ApiPermission(roles = {ROLE_MASTER, ROLE_HUB_MANAGER})
    @Operation(summary = "Order 취소", description = "Order 취소 api 입니다.")
    @DeleteMapping("/{id}/cancel")
    public void cancelOrder(
        @PathVariable("id") UUID orderId
//        @RequestHeader("X-User-Id") Long userId
    ) {
        Long userId = 1L;
        ordersFacade.cancelOrder(orderId, userId);
    }

    @ApiPermission(roles = {ROLE_MASTER, ROLE_HUB_MANAGER})
    @Operation(summary = "Order 삭제", description = "Order 삭제 api 입니다.")
    @DeleteMapping("/{id}")
    public void deleteOrder(
        @PathVariable("id") UUID orderId
//        @RequestHeader("X-User-Id") Long userId
    ) {
        Long userId = 1L;
        ordersFacade.deleteOrder(orderId, userId);
    }
}
