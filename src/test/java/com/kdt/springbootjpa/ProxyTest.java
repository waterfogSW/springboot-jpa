package com.kdt.springbootjpa;

import com.kdt.springbootjpa.member.model.Member;
import com.kdt.springbootjpa.order.model.Order;
import com.kdt.springbootjpa.order.model.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.kdt.springbootjpa.order.model.OrderStatus.OPENED;

@Slf4j
@SpringBootTest
public class ProxyTest {

    @Autowired
    private EntityManagerFactory emf;

    private String uuid;

    @BeforeEach
    void setUp() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        uuid = UUID.randomUUID().toString();

        // 주문 엔티티
        Order order = new Order();
        order.setUuid(uuid);
        order.setMemo("부재시 전화주세요.");
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);

        entityManager.persist(order);

        // 회원 엔티티
        Member member = new Member();
        member.setName("kanghonggu");
        member.setNickName("guppy.kang");
        member.setAge(33);
        member.setAddress("서울시 동작구만 움직이면쏜다.");
        member.setDescription("KDT 화이팅");

        member.addOrder(order); // 연관관계 편의 메소드 사용
        entityManager.persist(member);
        transaction.commit();
    }

    @Test
    void proxy() {
        EntityManager entityManager = emf.createEntityManager();

        // 회원 조회 -> 회원의 주문 까지 조회
        Member findMember = entityManager.find(Member.class, 1L);

        log.info("orders is loaded : {}", entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(findMember.getOrders()));

        log.info("-------");
        log.info("{}" ,findMember.getOrders().get(0).getMemo());
        log.info("orders is loaded : {}", entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(findMember.getOrders()));
    }

    @Test
    void move_persist() {
        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();

        final var order = em.find(Order.class, uuid);

        tx.begin();
        OrderItem item = new OrderItem();
        item.setQuantity(10);
        item.setPrice(1000);

        order.addOrderItem(item);
        tx.commit();
        em.clear();

        //////

        Order order2 = em.find(Order.class, uuid);
        tx.begin();
        order2.getOrderItems().remove(0);
        tx.commit();
    }
}
