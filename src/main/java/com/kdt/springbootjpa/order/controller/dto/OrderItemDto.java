package com.kdt.springbootjpa.order.controller.dto;

import com.kdt.springbootjpa.item.controller.dto.ItemDto;
import lombok.Builder;

import java.util.List;

public record OrderItemDto(
        Long id,
        Integer price,
        Integer quantity,
        List<ItemDto> itemDtos
) {
    @Builder
    public OrderItemDto {
    }
}
