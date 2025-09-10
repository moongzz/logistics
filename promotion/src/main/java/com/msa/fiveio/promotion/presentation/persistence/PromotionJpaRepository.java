package com.msa.fiveio.promotion.presentation.persistence;

import com.msa.fiveio.promotion.model.entity.Promotion;
import com.msa.fiveio.promotion.model.repository.PromotionRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromotionJpaRepository extends JpaRepository<Promotion, UUID>, PromotionRepository {
}
