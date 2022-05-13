package com.kdt.springbootjpa.common.converter;

import com.kdt.springbootjpa.item.controller.dto.ItemDto;
import com.kdt.springbootjpa.item.controller.dto.ItemType;
import com.kdt.springbootjpa.item.model.Car;
import com.kdt.springbootjpa.item.model.Food;
import com.kdt.springbootjpa.item.model.Furniture;
import com.kdt.springbootjpa.item.model.Item;
import com.kdt.springbootjpa.member.controller.dto.MemberDto;
import com.kdt.springbootjpa.member.model.Member;
import com.kdt.springbootjpa.order.controller.dto.OrderDto;
import com.kdt.springbootjpa.order.controller.dto.OrderItemDto;
import com.kdt.springbootjpa.order.model.Order;
import com.kdt.springbootjpa.order.model.OrderItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoConverter {

    public Order convertOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setUuid(orderDto.uuid());
        order.setMemo(orderDto.memo());
        order.setOrderStatus(orderDto.orderStatus());
        order.setOrderDatetime(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedBy(orderDto.memberDto().name());

        order.setMember(this.convertMember(orderDto.memberDto()));
        this.convertOrderItems(orderDto).forEach(order::addOrderItem);

        return order;
    }

    private Member convertMember(MemberDto memberDto) {
        Member member = new Member();
        member.setName(memberDto.name());
        member.setNickName(memberDto.nickName());
        member.setAge(memberDto.age());
        member.setAddress(memberDto.address());
        member.setDescription(memberDto.description());

        return member;
    }

    private List<OrderItem> convertOrderItems(OrderDto orderDto) {
        return orderDto.orderItemDtos().stream()
                .map(orderItemDto -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setPrice(orderItemDto.price());
                    orderItem.setQuantity(orderItemDto.quantity());
                    List<Item> items = orderItemDto.itemDtos().stream()
                            .map(this::convertItem)
                            .collect(Collectors.toList());
                    items.forEach(orderItem::addItem);
                    return orderItem;
                })
                .collect(Collectors.toList());
    }

    private Item convertItem(ItemDto itemDto) {
        if (ItemType.FOOD.equals(itemDto.type())) {
            Food food = new Food();
            food.setPrice(itemDto.price());
            food.setStockQuantity(itemDto.stockQuantity());
            food.setChef(itemDto.chef());
            return food;
        }

        if (ItemType.FURNITURE.equals(itemDto.type())) {
            Furniture furniture = new Furniture();
            furniture.setPrice(itemDto.price());
            furniture.setStockQuantity(itemDto.stockQuantity());
            furniture.setHeight(itemDto.height());
            furniture.setWidth(itemDto.width());
            return furniture;
        }

        if (ItemType.CAR.equals(itemDto.type())) {
            Car car = new Car();
            car.setPrice(itemDto.price());
            car.setStockQuantity(itemDto.stockQuantity());
            car.setPower(itemDto.power());
            return car;
        }

        throw new IllegalArgumentException("잘못된 아이템 타입 입니다.");
    }


    public OrderDto convertOrderDto(Order order) {
        return OrderDto.builder()
                .uuid(order.getUuid())
                .memo(order.getMemo())
                .orderStatus(order.getOrderStatus())
                .orderDatetime(order.getOrderDatetime())
                .memberDto(this.convertMemberDto(order.getMember()))
                .orderItemDtos(order.getOrderItems().stream()
                        .map(this::convertOrderItemDto)
                        .collect(Collectors.toList())
                )
                .build();
    }


    private MemberDto convertMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .nickName(member.getNickName())
                .description(member.getDescription())
                .age(member.getAge())
                .address(member.getAddress())
                .build();
    }

    private OrderItemDto convertOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .itemDtos(orderItem.getItems().stream()
                        .map(this::convertItemDto)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private ItemDto convertItemDto(Item item) {
        if (item instanceof Food) {
            return ItemDto.builder()
                    .id(item.getId())
                    .type(ItemType.FOOD)
                    .stockQuantity(item.getStockQuantity())
                    .price(item.getPrice())
                    .chef(((Food) item).getChef())
                    .build();
        }

        if (item instanceof Furniture) {
            return ItemDto.builder()
                    .id(item.getId())
                    .type(ItemType.FURNITURE)
                    .stockQuantity(item.getStockQuantity())
                    .price(item.getPrice())
                    .width(((Furniture) item).getWidth())
                    .width(((Furniture) item).getHeight())
                    .build();
        }

        if (item instanceof Car) {
            return ItemDto.builder()
                    .id(item.getId())
                    .type(ItemType.CAR)
                    .stockQuantity(item.getStockQuantity())
                    .price(item.getPrice())
                    .power(((Car) item).getPower())
                    .build();
        }

        throw new IllegalArgumentException("잘못된 아이템 타입 입니다.");
    }

}
