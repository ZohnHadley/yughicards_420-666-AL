package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private Long id;
    private YughioCardDTO card;
    private int quantity;

    public static CartItemDTO of(CartItem item) {
        return CartItemDTO.builder()
                .id(item.getId())
                .card(YughioCardDTO.of(item.getCard()))
                .quantity(item.getQuantity())
                .build();
    }
}