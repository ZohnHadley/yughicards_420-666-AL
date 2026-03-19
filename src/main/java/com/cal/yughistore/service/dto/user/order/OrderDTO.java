// service/dto/order/OrderDTO.java
package com.cal.yughistore.service.dto.user.order;


import com.cal.yughistore.model.user.order.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDTO {

    private Long id;
    private String shippingMethod;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private int itemCount;           // nb de lignes distinctes
    private int totalCards;          // somme des quantités
    private List<OrderItemDTO> items;

    public static OrderDTO from(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.id             = order.getId();
        dto.shippingMethod = order.getShippingMethod();
        dto.totalPrice     = order.getTotalPrice();
        dto.createdAt      = order.getCreatedAt();
        dto.items          = order.getItems()
                .stream()
                .map(OrderItemDTO::from)
                .collect(Collectors.toList());
        dto.itemCount      = dto.items.size();
        dto.totalCards     = dto.items.stream()
                .mapToInt(OrderItemDTO::getQuantity)
                .sum();
        return dto;
    }
}