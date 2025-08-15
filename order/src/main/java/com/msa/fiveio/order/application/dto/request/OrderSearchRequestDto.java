package com.msa.fiveio.order.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderSearchRequestDto {

    @JsonProperty("requester-company-id")
    private final UUID requesterCompanyId;

    @JsonProperty("receiver-company-id")
    private final UUID receiverCompanyId;

    @JsonProperty("product-id")
    private final UUID productId;
}
