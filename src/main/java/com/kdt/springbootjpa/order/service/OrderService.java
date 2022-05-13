package com.kdt.springbootjpa.order.service;

import com.kdt.springbootjpa.common.converter.DtoConverter;
import com.kdt.springbootjpa.common.exception.NotFoundException;
import com.kdt.springbootjpa.order.controller.dto.OrderDto;
import com.kdt.springbootjpa.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    public OrderService(OrderRepository orderRepository, DtoConverter dtoConverter) {
        this.orderRepository = orderRepository;
        this.dtoConverter = dtoConverter;
    }

    @Transactional
    public String save(OrderDto dto) {
        final var order = dtoConverter.convertOrder(dto);
        final var entity = orderRepository.save(order);
        return entity.getUuid();
    }

    @Transactional
    public Page<OrderDto> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(dtoConverter::convertOrderDto);
    }

    @Transactional
    public OrderDto findOne(String uuid) {
        return orderRepository.findById(uuid)
                .map(dtoConverter::convertOrderDto)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다"));
    }
}
