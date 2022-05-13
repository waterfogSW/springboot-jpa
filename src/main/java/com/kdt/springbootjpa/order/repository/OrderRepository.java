package com.kdt.springbootjpa.order.repository;

import com.kdt.springbootjpa.order.model.Order;
import com.kdt.springbootjpa.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    List<Order> findAllByOrderStatusOrderByOrderDatetime(OrderStatus orderStatus);

    @Query("SELECT o FROM Order AS o WHERE o.memo LIKE %?1%")
    Optional<Order> findByMemo(String memo);
}
