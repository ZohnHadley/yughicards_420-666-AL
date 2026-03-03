package com.cal.yughistore.services.dto.shoppingcart;


import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.model.applicaitonuser.ApplicationUser;
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
@ToString
public class ShoppingCartDTO {
    private Long id;
    private ApplicationUserDTO applicationUser;
    private List<YughioCardDTO> cards;


    public static ShoppingCartDTO of(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            return null;
        }

        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .applicationUser(ApplicationUserDTO.of(shoppingCart.getApplicationUser()))
                .cards(shoppingCart.getCardList().stream().map(YughioCardDTO::of).toList())
                .build();
    }

    public ShoppingCart toShoppingCart() {
        ApplicationUser userRef = null;
        if (this.applicationUser != null && this.applicationUser.getId() != null) {
            userRef = new ApplicationUser();
            userRef.setId(this.applicationUser.getId());
        }

        ShoppingCart cart = ShoppingCart.builder()
                .id(this.getId())
                .applicationUser(userRef)
                .cardList(this.cards == null
                        ? java.util.List.of()
                        : this.cards.stream().map(YughioCardDTO::toYughioCard).toList())
                .build();

        if (userRef != null) {
            userRef.setShoppingCart(cart);
        }

        return cart;
    }
}
