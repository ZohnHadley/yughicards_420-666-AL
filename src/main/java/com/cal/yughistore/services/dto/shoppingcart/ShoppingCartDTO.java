package com.cal.yughistore.services.dto.shoppingcart;


import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.services.dto.applicationuser.ApplicationUserDTO;
import com.cal.yughistore.services.dto.yughiocard.YughioCardDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCartDTO {
    private Long id;
    private Long applicationUserID;
    private List<YughioCardDTO> cards;


    public static ShoppingCartDTO of(ShoppingCart shoppingCart) {
        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .applicationUserID(shoppingCart.getApplicationUser().getId())
                .cards(shoppingCart.getCardList().stream().map(YughioCardDTO::of).toList())
                .build();
    }

    public ShoppingCart toShoppingCart() {
        return ShoppingCart.builder()
                .id(this.getId())
                .cardList(this.cards.stream().map(YughioCardDTO::toYughioCard).toList())
                .build();
    }
}
