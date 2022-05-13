package com.kdt.springbootjpa.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt.springbootjpa.item.controller.dto.ItemDto;
import com.kdt.springbootjpa.item.controller.dto.ItemType;
import com.kdt.springbootjpa.member.controller.dto.MemberDto;
import com.kdt.springbootjpa.order.controller.dto.OrderDto;
import com.kdt.springbootjpa.order.controller.dto.OrderItemDto;
import com.kdt.springbootjpa.order.model.OrderStatus;
import com.kdt.springbootjpa.order.repository.OrderRepository;
import com.kdt.springbootjpa.order.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class OrderApiControllerTest {

    String uuid = UUID.randomUUID().toString();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        // Given
        OrderDto orderDto = OrderDto.builder()
                .uuid(uuid)
                .memo("문앞 보관 해주세요.")
                .orderDatetime(LocalDateTime.now())
                .orderStatus(OrderStatus.OPENED)
                .memberDto(
                        MemberDto.builder()
                                .name("강홍구")
                                .nickName("guppy.kang")
                                .address("서울시 동작구만 움직이면 쏜다.")
                                .age(33)
                                .description("---")
                                .build()
                )
                .orderItemDtos(List.of(
                        OrderItemDto.builder()
                                .price(1000)
                                .quantity(100)
                                .itemDtos(List.of(
                                        ItemDto.builder()
                                                .type(ItemType.FOOD)
                                                .chef("백종원")
                                                .price(1000)
                                                .build()
                                ))
                                .build()
                ))
                .build();
        // When
        orderService.save(orderDto);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/api/v1/orders")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void getOne() throws Exception {
        mockMvc.perform(get("/api/v1/orders/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void save() throws Exception {
        //given
        OrderDto orderDto = OrderDto.builder()
                .uuid(UUID.randomUUID().toString())
                .memo("문앞 보관 해주세요.")
                .orderDatetime(LocalDateTime.now())
                .orderStatus(OrderStatus.OPENED)
                .memberDto(
                        MemberDto.builder()
                                .name("강홍구")
                                .nickName("guppy.hong")
                                .address("서울시 동작구만 움직이면 쏜다.")
                                .age(33)
                                .description("---")
                                .build()
                )
                .orderItemDtos(List.of(
                        OrderItemDto.builder()
                                .price(1000)
                                .quantity(100)
                                .itemDtos(List.of(
                                        ItemDto.builder()
                                                .type(ItemType.FOOD)
                                                .chef("백종원")
                                                .price(1000)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("order-save",
                        requestFields(
                                fieldWithPath("uuid").type(JsonFieldType.STRING)
                                        .description("UUID"),
                                fieldWithPath("orderDatetime").type(JsonFieldType.STRING)
                                        .description("orderDateTime"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING)
                                        .description("orderStatus"),
                                fieldWithPath("memo").type(JsonFieldType.STRING)
                                        .description("memo"),
                                fieldWithPath("memberDto").type(JsonFieldType.OBJECT)
                                        .description("memberDto"),
                                fieldWithPath("memberDto.id").type(JsonFieldType.NULL)
                                        .description("memberDto.id"),
                                fieldWithPath("memberDto.name").type(JsonFieldType.STRING)
                                        .description("memberDto.name"),
                                fieldWithPath("memberDto.nickName").type(JsonFieldType.STRING)
                                        .description("memberDto.nickName"),
                                fieldWithPath("memberDto.age").type(JsonFieldType.NUMBER)
                                        .description("memberDto.age"),
                                fieldWithPath("memberDto.address").type(JsonFieldType.STRING)
                                        .description("memberDto.address"),
                                fieldWithPath("memberDto.description").type(JsonFieldType.STRING)
                                        .description("memberDto.description"),
                                fieldWithPath("orderItemDtos[]").type(JsonFieldType.ARRAY)
                                        .description("orderItemDtos[]"),
                                fieldWithPath("orderItemDtos[].id").type(JsonFieldType.NULL)
                                        .description("orderItemDtos[]"),
                                fieldWithPath("orderItemDtos[].price").type(JsonFieldType.NUMBER)
                                        .description("orderItemDtos[]"),
                                fieldWithPath("orderItemDtos[].quantity").type(JsonFieldType.NUMBER)
                                        .description("orderItemDtos[]"),
                                fieldWithPath("orderItemDtos[].itemDtos[]").type(JsonFieldType.ARRAY)
                                        .description("orderItemDtos[].itemDtos[]"),
                                fieldWithPath("orderItemDtos[].itemDtos[].id").type(JsonFieldType.NULL)
                                        .description("orderItemDtos[].itemDtos[].id"),
                                fieldWithPath("orderItemDtos[].itemDtos[].power").type(JsonFieldType.NULL)
                                        .description("orderItemDtos[].itemDtos[].power"),
                                fieldWithPath("orderItemDtos[].itemDtos[].width").type(JsonFieldType.NULL)
                                        .description("orderItemDtos[].itemDtos[].width"),
                                fieldWithPath("orderItemDtos[].itemDtos[].height").type(JsonFieldType.NULL)
                                        .description("orderItemDtos[].itemDtos[].height"),
                                fieldWithPath("orderItemDtos[].itemDtos[].price").type(JsonFieldType.NUMBER)
                                        .description("orderItemDtos[].itemDtos[].price"),
                                fieldWithPath("orderItemDtos[].itemDtos[].stockQuantity").type(JsonFieldType.NUMBER)
                                        .description("orderItemDtos[].itemDtos[].stockQuantity"),
                                fieldWithPath("orderItemDtos[].itemDtos[].type").type(JsonFieldType.STRING)
                                        .description("orderItemDtos[].itemDtos[].type"),
                                fieldWithPath("orderItemDtos[].itemDtos[].chef").type(JsonFieldType.STRING)
                                        .description("orderItemDtos[].itemDtos[].chef")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
                                        .description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.STRING)
                                        .description("데이터"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("응답시간")
                        )
                ));
    }
}