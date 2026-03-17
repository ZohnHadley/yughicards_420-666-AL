package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.model.user.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO {
    private Long id = -1L;
    private Long applicationUserId;
    private String applicationUserEmail;

    @Builder.Default
    private List<CartItemDTO> cartItemsList = new ArrayList<>();

    public static ShoppingCartDTO of(ShoppingCart shoppingCart) {
        validateShoppingCart(shoppingCart);

        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .applicationUserId(shoppingCart.getApplicationUser().getId())
                .applicationUserEmail(shoppingCart.getApplicationUser().getEmail())
                .cartItemsList(mapCartItemsToDto(shoppingCart.getCartItemList()))
                .build();
    }

    public ShoppingCart toShoppingCart() {
        validateDto(this);

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(getApplicationUserId());

        List<CartItem> cartItems = mapCartItemsToEntity(getCartItemsList());

        ShoppingCart shoppingCart = ShoppingCart.builder()
                .id(getId())
                .applicationUser(applicationUser)
                .cartItemList(cartItems)
                .build();

        cartItems.forEach(cartItem -> cartItem.setShoppingCart(shoppingCart));

        return shoppingCart;
    }

    private static void validateShoppingCart(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new IllegalArgumentException("Shopping cart must not be null");
        }
        if (shoppingCart.getApplicationUser() == null) {
            throw new IllegalArgumentException("Shopping cart user must not be null");
        }
        if (shoppingCart.getApplicationUser().getId() == null) {
            throw new IllegalArgumentException("Shopping cart user id must not be null");
        }
        if (shoppingCart.getCartItemList() == null) {
            throw new IllegalArgumentException("Shopping cart items list must not be null");
        }
    }

    private static void validateDto(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO == null) {
            throw new IllegalArgumentException("Shopping cart DTO must not be null");
        }
        if (shoppingCartDTO.cartItemsList == null) {
            throw new IllegalArgumentException("Shopping cart items list must not be null");
        }
        if (shoppingCartDTO.applicationUserId == null) {
            throw new IllegalArgumentException("Shopping cart user id must not be null");
        }
    }

    private static List<CartItemDTO> mapCartItemsToDto(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return List.of();
        }

        return cartItems.stream()
                .map(CartItemDTO::of)
                .toList();
    }

    private static List<CartItem> mapCartItemsToEntity(List<CartItemDTO> cartItemDTOs) {
        if (cartItemDTOs == null || cartItemDTOs.isEmpty()) {
            return List.of();
        }

        return cartItemDTOs.stream()
                .map(CartItemDTO::toCartItem)
                .toList();
    }
}
