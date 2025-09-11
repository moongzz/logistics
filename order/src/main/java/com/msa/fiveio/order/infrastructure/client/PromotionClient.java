package com.msa.fiveio.order.infrastructure.client;

import com.msa.fiveio.order.infrastructure.client.dto.response.PromotionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "promotionClient", url = "http://localhost:19099")
public interface PromotionClient {

    @GetMapping("/api/promotions/{product-id}")
    PromotionDto getByProductId(@PathVariable("product-id") UUID productId);
}
