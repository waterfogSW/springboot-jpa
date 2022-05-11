package com.kdt.springbootjpa.member.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManagerFactory;

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
}