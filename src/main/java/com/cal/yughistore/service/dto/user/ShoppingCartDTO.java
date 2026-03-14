package com.cal.yughistore.service.dto.user;


import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
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
    @NotBlank
    private UserPublicDTO applicationUser;
    @Builder.Default
    private List<CartItemDTO> cartItemsList = new ArrayList<>();


    public static ShoppingCartDTO of(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new IllegalArgumentException("Shopping cart must not be null");
        }
        if (shoppingCart.getCartItemList() == null) {
            throw new IllegalArgumentException("Shopping cart list cannot be null");
        }

        List<CartItemDTO> cardList = new ArrayList<>();

        if (shoppingCart.getCartItemList() != null && !shoppingCart.getCartItemList().isEmpty()) {
            for (CartItem cartItem : shoppingCart.getCartItemList()) {
                cardList.add(CartItemDTO.of(cartItem));
            }
        }

        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .applicationUser(UserPublicDTO.of(shoppingCart.getApplicationUser()))
                .cartItemsList(cardList)
                .build();
    }

    public ShoppingCart toShoppingCart() {

        ApplicationUser userRef = null;
        if (this.applicationUser != null && this.applicationUser.getId() != null) {
            userRef = new ApplicationUser();
            userRef.setId(this.applicationUser.getId());
        }

        List<CartItem> itemList = new ArrayList<>();

        if (this.getCartItemsList() != null) {
            for (CartItemDTO cartItem : this.getCartItemsList()) {
                itemList.add(cartItem.toCartItem());
            }
        }

        ShoppingCart cart = ShoppingCart.builder()
                .id(this.getId())
                .applicationUser(userRef)
                .cartItemList(itemList)
                .build();

        if (userRef != null) {
            userRef.setShoppingCart(cart);
        }

        return cart;
    }
}
