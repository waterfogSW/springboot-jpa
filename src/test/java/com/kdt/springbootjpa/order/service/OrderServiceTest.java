package com.kdt.springbootjpa.order.service;

import com.kdt.springbootjpa.item.controller.dto.ItemDto;
import com.kdt.springbootjpa.item.controller.dto.ItemType;
import com.kdt.springbootjpa.member.controller.dto.MemberDto;
import com.kdt.springbootjpa.order.controller.dto.OrderDto;
import com.kdt.springbootjpa.order.controller.dto.OrderItemDto;
import com.kdt.springbootjpa.order.model.OrderStatus;
import com.kdt.springbootjpa.order.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OrderServiceTest {
    private final String uuid = UUID.randomUUID().toString();

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void save_test() {
        orderRepository.deleteAll();

        final var memberDto = MemberDto.builder()
                .name("김산")
                .nickName("waterfogsw")
                .address("경기도 용인시")
                .age(26)
                .description("---")
                .build();

        final var itemDto = ItemDto.builder()
                .type(ItemType.FOOD)
                .chef("백종원")
                .price(1000)
                .build();

        final var orderItemDto = OrderItemDto.builder()
                .price(1000)
                .quantity(100)
                .itemDtos(List.of(itemDto))
                .build();

        final var orderDto = OrderDto.builder()
                .uuid(uuid)
                .memo("문앞 보관 해주세요.")
                .orderDatetime(LocalDateTime.now())
                .orderStatus(OrderStatus.OPENED)
                .memberDto(memberDto)
                .orderItemDtos(List.of(orderItemDto))
                .build();

        final var savedUuid = orderService.save(orderDto);

        assertThat(uuid).isEqualTo(savedUuid);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("findOneTest")
    public void findOneTest() {
        // when
        final var found = orderService.findOne(uuid);

        // then
        assertThat(found.uuid()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("findAllTest")
    public void findAllTest() {
        // given
        PageRequest page = PageRequest.of(0, 10);

        // when
        Page<OrderDto> all = orderService.findAll(page);

        // then
        assertThat(all.getTotalElements()).isEqualTo(1);

    }
}