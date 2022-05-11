package com.kdt.springbootjpa.member.model;

import com.kdt.springbootjpa.order.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
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
class MemberTest {
    @Autowired
    EntityManagerFactory emf;

    @Test
    @DisplayName("persist 테스트")
    public void persist() {
        Member member = new Member();
        member.setName("san");
        member.setAddress("서울시 동작구(만) 움직이면 쏜다");
        member.setAge(33);
        member.setNickName("san9580");
        member.setDescription("백앤드 개발자에요");

        final var em = emf.createEntityManager();
        final var tx = em.getTransaction();
        tx.begin();

        em.persist(member);

        tx.commit();
    }

    @Test
    void 잘못된_설계() {
        Member member = new Member();
        member.setName("kanghonggu");
        member.setAddress("서울시 동작구(만) 움직이면 쏜다.");
        member.setAge(33);
        member.setNickName("guppy.kang");
        member.setDescription("백앤드 개발자에요.");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(member);
        Member memberEntity = entityManager.find(Member.class, 1L);

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);
        order.setMemo("부재시 전화주세요.");
        order.setMemberId(memberEntity.getId()); // 외래키를 직접 지정

        entityManager.persist(order);
        transaction.commit();

        Order orderEntity = entityManager.find(Order.class, order.getUuid());
        // FK 를 이용해 회원 다시 조회
        Member orderMemberEntity = entityManager.find(Member.class, orderEntity.getMemberId());
        // orderEntity.getMember() // 객체중심 설계라면 이렇게 해야하지 않을까?
        log.info("nick : {}", orderMemberEntity.getNickName());
    }
}