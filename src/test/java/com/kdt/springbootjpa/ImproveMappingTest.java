package com.kdt.springbootjpa;

import com.kdt.springbootjpa.common.model.Parent;
import com.kdt.springbootjpa.common.model.ParentId;
import com.kdt.springbootjpa.item.model.Food;
import com.kdt.springbootjpa.order.model.Order;
import com.kdt.springbootjpa.order.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class ImproveMappingTest {

    @Autowired
    private EntityManagerFactory emf;

    @Test
    @DisplayName("상속관계 테스트")
    public void inheritance_test() {
        // given
        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();

        tx.begin();
        // when
        Food food = new Food();
        food.setPrice(2000);
        food.setStockQuantity(200);
        food.setChef("백종원");

        em.persist(food);
        // then
        tx.commit();
    }

    @Test
    @DisplayName("super class 테스트")
    public void mapped_super_class_test() {
        // given
        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();

        tx.begin();

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderStatus(OrderStatus.OPENED);
        order.setMemo("---");
        order.setOrderDatetime(LocalDateTime.now());

        tx.commit();
    }

    @Test
    void id_test() {
        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();

        tx.begin();

        final var parent = new Parent();
        parent.setId(new ParentId("id1", "id2"));

        em.persist(parent);
        tx.commit();

        em.clear();
        final var findParent = em.find(Parent.class, new ParentId("id1", "id2"));
        log.info("{} {}", findParent.getId().getId1(), findParent.getId().getId2());
    }

}
