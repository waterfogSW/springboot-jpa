package com.kdt.springbootjpa.order.controller;

import com.kdt.springbootjpa.common.dto.ApiResponse;
import com.kdt.springbootjpa.common.exception.NotFoundException;
import com.kdt.springbootjpa.order.service.OrderService;
import com.kdt.springbootjpa.order.controller.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> save(@RequestBody OrderDto orderDto) {
        String uuid = orderService.save(orderDto);
        return ApiResponse.ok(uuid);
    }

    @GetMapping("/{uuid}")
    public ApiResponse<OrderDto> getOne(@PathVariable String uuid) {
        OrderDto one = orderService.findOne(uuid);
        return ApiResponse.ok(one);
    }

    @GetMapping
    public ApiResponse<Page<OrderDto>> getAll(Pageable pageable) {
        Page<OrderDto> all = orderService.findAll(pageable);
        return ApiResponse.ok(all);
    }
}
