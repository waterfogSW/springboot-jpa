package com.kdt.springbootjpa.item.controller.dto;

import lombok.Builder;

public record ItemDto(
        Long id,
        int price,
        int stockQuantity,

        ItemType type,

        // Food
        String chef,
        // Car
        Integer power,
        // Furniture
        Integer width,
        Integer height
) {
    @Builder
    public ItemDto {
    }
}
