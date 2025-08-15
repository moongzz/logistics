package com.msa.fiveio.order.application.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {

    private final UUID orderId;

}
