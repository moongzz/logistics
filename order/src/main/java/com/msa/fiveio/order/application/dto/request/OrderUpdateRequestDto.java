package com.msa.fiveio.order.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderUpdateRequestDto {

    @JsonProperty("quantity")
    private final Long quantity;
    @JsonProperty("request-notes")
    private final String requestNotes;
}
