package com.kdt.springbootjpa.order.controller.dto;

import com.kdt.springbootjpa.member.controller.dto.MemberDto;
import com.kdt.springbootjpa.order.model.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        String uuid,
        LocalDateTime orderDatetime,
        OrderStatus orderStatus,
        String memo,

        MemberDto memberDto,
        List<OrderItemDto> orderItemDtos
) {
    @Builder
    public OrderDto {
    }
}
