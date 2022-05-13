package com.kdt.springbootjpa.order.repository;

import com.kdt.springbootjpa.order.model.Order;
import com.kdt.springbootjpa.order.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void test() {
        String uuid = UUID.randomUUID().toString();

        Order order = new Order();
        order.setUuid(uuid);
        order.setOrderStatus(OrderStatus.OPENED);
        order.setOrderDatetime(LocalDateTime.now());
        order.setMemo("----");
        order.setCreatedBy("guppy.kanng");
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        List<Order> findOrders = orderRepository.findAll();

        orderRepository.findAllByOrderStatus(OrderStatus.OPENED);
        orderRepository.findAllByOrderStatusOrderByOrderDatetime(OrderStatus.OPENED);
        orderRepository.findByMemo("----");
    }
}