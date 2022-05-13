package com.kdt.springbootjpa.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kdt.springbootjpa.common.exception.ControllerAdvisor;
import com.kdt.springbootjpa.item.controller.dto.ItemDto;
import com.kdt.springbootjpa.item.controller.dto.ItemType;
import com.kdt.springbootjpa.member.controller.dto.MemberDto;
import com.kdt.springbootjpa.order.controller.dto.OrderDto;
import com.kdt.springbootjpa.order.controller.dto.OrderItemDto;
import com.kdt.springbootjpa.order.model.OrderStatus;
import com.kdt.springbootjpa.order.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderApiControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderApiController orderApiController;

    private MockMvc mockMvc;
    private OrderDto orderDto;
    private ItemDto itemDto;
    private MemberDto memberDto;
    private OrderItemDto orderItemDto;
    private String uuid = UUID.randomUUID().toString();

    @BeforeAll
    public static void beforeAll() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderApiController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(ControllerAdvisor.class)
                .build();

        memberDto = MemberDto.builder()
                .name("김산")
                .nickName("waterfogsw")
                .address("경기도 용인시")
                .age(26)
                .description("---")
                .build();

        itemDto = ItemDto.builder()
                .type(ItemType.FOOD)
                .chef("백종원")
                .price(1000)
                .build();

        orderItemDto = OrderItemDto.builder()
                .price(1000)
                .quantity(100)
                .itemDtos(List.of(itemDto))
                .build();

        orderDto = OrderDto.builder()
                .uuid(uuid)
                .memo("문앞 보관 해주세요.")
                .orderDatetime(LocalDateTime.now())
                .orderStatus(OrderStatus.OPENED)
                .memberDto(memberDto)
                .orderItemDtos(List.of(orderItemDto))
                .build();
    }


    @Test
    public void save() throws Exception {
        when(orderService.save(any())).thenReturn(UUID.randomUUID().toString());

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void getOne() throws Exception {
        when(orderService.findOne(any())).thenReturn(orderDto);

        mockMvc.perform(get("/api/v1/orders/{uuid}", uuid))
                .andExpect(status().isOk())
                .andDo(print());
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
}