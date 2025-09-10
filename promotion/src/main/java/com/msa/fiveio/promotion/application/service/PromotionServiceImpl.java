package com.msa.fiveio.promotion.application.service;

import com.msa.fiveio.promotion.application.dto.request.ReqPromotionPostDto;
import com.msa.fiveio.promotion.application.dto.response.ResPromotionGetByProductIdDto;
import com.msa.fiveio.promotion.model.entity.Promotion;
import com.msa.fiveio.promotion.model.repository.PromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository repository;

    @Transactional
    @Override
    public void postBy(ReqPromotionPostDto dto) {
        repository.save(dto.create());
    }

    @Override
    public ResPromotionGetByProductIdDto getBy(UUID productId) {
        Promotion promotion = repository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 프로모션입니다."));
        return ResPromotionGetByProductIdDto.of(promotion);
    }
}
