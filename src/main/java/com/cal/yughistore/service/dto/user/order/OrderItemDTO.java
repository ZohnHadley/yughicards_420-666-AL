// service/dto/order/OrderItemDTO.java
package com.cal.yughistore.service.dto.user.order;

import com.cal.yughistore.model.user.order.OrderItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {

    private Long cardId;
    private String cardName;
    private String cardType;
    private String imageUrl;
    private String frameType;
    private int quantity;
    private BigDecimal priceAtPurchase;  // prix unitaire CAD
    private BigDecimal lineTotal;        // priceAtPurchase × quantity

    public static OrderItemDTO from(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.cardId           = item.getCard().getId();
        dto.cardName         = item.getCard().getName();
        dto.cardType         = item.getCard().getType().name();
        dto.frameType        = item.getCard().getFrameType().name();
        dto.quantity         = item.getQuantity();
        dto.priceAtPurchase  = item.getPriceAtPurchase();
        dto.lineTotal        = item.getPriceAtPurchase()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        // Première image si disponible
        if (item.getCard().getCard_images() != null
                && !item.getCard().getCard_images().isEmpty()) {
            dto.imageUrl = item.getCard().getCard_images().get(0).getImage_url_small();
        }

        return dto;
    }
}