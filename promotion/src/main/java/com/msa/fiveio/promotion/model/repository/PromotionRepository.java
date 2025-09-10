package com.msa.fiveio.promotion.model.repository;

import com.msa.fiveio.promotion.model.entity.Promotion;

import java.util.Optional;
import java.util.UUID;

public interface PromotionRepository {
    Promotion save(Promotion promotion);
    Optional<Promotion> findByProductId(UUID productId);
}
