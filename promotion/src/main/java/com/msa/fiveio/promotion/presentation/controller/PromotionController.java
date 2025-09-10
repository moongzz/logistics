package com.msa.fiveio.promotion.presentation.controller;

import com.msa.fiveio.promotion.application.dto.request.ReqPromotionPostDto;
import com.msa.fiveio.promotion.application.dto.response.ResPromotionGetByProductIdDto;
import com.msa.fiveio.promotion.application.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotions")
public class PromotionController {
    private final PromotionService service;

    @PostMapping
    public void PostBy(
            @RequestBody ReqPromotionPostDto request
    ) {
        service.postBy(request);
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ResPromotionGetByProductIdDto> getBy(
            @PathVariable("product-id") UUID productId
    ) {
        return ResponseEntity.ok(service.getBy(productId));
    }
}
