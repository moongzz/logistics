package com.msa.fiveio.order.presentation.controller;

import com.msa.fiveio.order.application.dto.request.OrderCreateRequestDto;
import com.msa.fiveio.order.application.dto.response.OrderCreateResponseDto;
import com.msa.fiveio.order.application.facade.OrdersFacade;
import com.msa.fiveio.order.application.usecase.DeliveryService;
import com.msa.fiveio.order.application.usecase.ProductService;
import com.msa.fiveio.order.application.usecase.PromotionService;
import com.msa.fiveio.order.infrastructure.client.dto.response.ProductResponseDto;
import com.msa.fiveio.order.infrastructure.client.dto.response.PromotionDto;
import com.msa.fiveio.order.model.entity.Order;
import com.msa.fiveio.order.model.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(false)
public class OrderControllerTest {

    @Autowired
    private OrdersFacade orderFacade;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private ProductService productService;

    @MockBean
    private PromotionService promotionService;

    private OrderCreateRequestDto request;
    private ProductResponseDto product;
    private PromotionDto promotion;
    private UUID productId;
    private UUID companyId;
    private Long userId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        userId = 1L;

        request = OrderCreateRequestDto.builder()
                .productId(productId)
                .receiverCompanyId(companyId)
                .quantity(10L)
                .requestNotes("빠른 배송 부탁드립니다.")
                .recipientName("짱구")
                .recipientSlackId("010-1234-1234")
                .build();

        product = ProductResponseDto.builder()
                .deliveryAddress("서울시 강남구")
                .requesterCompanyId(companyId)
                .departHubId(UUID.randomUUID())
                .arriveHubId(UUID.randomUUID())
                .productPrice(1000.0)
                .productName("테스트 상품")
                .build();

        promotion = PromotionDto.builder()
                .promotionId(UUID.randomUUID())
                .name("10% 할인")
                .discountType(PromotionDto.DiscountType.PERCENT)
                .discountValue(BigDecimal.valueOf(10))
                .build();
    }

    @Test
    void createOrder_실제_DB에_주문이_저장된다() {
        // Given
        when(productService.sendProductRequest(any(OrderCreateRequestDto.class)))
                .thenReturn(product);
        when(promotionService.getPromotion(productId))
                .thenReturn(promotion);
        doNothing().when(deliveryService)
                .sendDeliveryRequest(any(UUID.class), any(ProductResponseDto.class), any(OrderCreateRequestDto.class));

        // When
        OrderCreateResponseDto result = orderFacade.createOrder(request);

        // Then
        assertNotNull(result);
        assertNotNull(result.getOrderId());

        // DB에서 실제로 저장되었는지 확인
        Optional<Order> savedOrder = orderRepository.findById(result.getOrderId());
        assertTrue(savedOrder.isPresent());

        Order order = savedOrder.get();
        assertEquals(productId, order.getProductId());
        assertEquals(10L, order.getQuantity());
        assertNotNull(order.getCreatedAt());

        // 외부 서비스 호출 검증
        verify(productService, times(1)).sendProductRequest(any(OrderCreateRequestDto.class));
        verify(promotionService, times(1)).getPromotion(productId);
        verify(deliveryService, times(1)).sendDeliveryRequest(any(UUID.class), eq(product), any(OrderCreateRequestDto.class));
    }

    @Test
    void createOrder_프로모션_적용후_가격이_정상적으로_계산된다() {
        // Given
        when(productService.sendProductRequest(any(OrderCreateRequestDto.class)))
                .thenReturn(product);
        when(promotionService.getPromotion(productId))
                .thenReturn(promotion);
        doNothing().when(deliveryService)
                .sendDeliveryRequest(any(UUID.class), any(ProductResponseDto.class), any(OrderCreateRequestDto.class));

        // When
        OrderCreateResponseDto result = orderFacade.createOrder(request);

        // Then
        Optional<Order> savedOrder = orderRepository.findById(result.getOrderId());
        assertTrue(savedOrder.isPresent());

        Order order = savedOrder.get();

        // 프로모션 할인이 적용되었는지 확인
        double expectedOriginalPrice = product.getProductPrice() * 10;
        double expectedDiscountedPrice = expectedOriginalPrice * 0.9; // 10% 할인

        assertEquals(expectedDiscountedPrice, order.getTotalAmount());
    }
}
