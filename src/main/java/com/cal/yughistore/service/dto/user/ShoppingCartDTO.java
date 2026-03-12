package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
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
    private UserPublicDTO applicationUser;

    // Chaque entrée représente UNE carte avec sa quantité
    private List<CartItemDTO> items;

    // ── of(ShoppingCart) ────────────────────────────────────────────────────
    public static ShoppingCartDTO of(ShoppingCart cart) {
        if (cart == null) throw new IllegalArgumentException("Shopping cart must not be null");

        List<CartItemDTO> items = new ArrayList<>();
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                items.add(CartItemDTO.of(item));
            }
        }

        return ShoppingCartDTO.builder()
                .id(cart.getId())
                .applicationUser(UserPublicDTO.of(cart.getApplicationUser()))
                .items(items)
                .build();
    }

    // ── Retourne une liste plate de cartes (une entrée par quantité) ────────
    // Utilisé par les anciens endpoints qui retournent List<YughioCardDTO>
    public List<YughioCardDTO> getCards() {
        List<YughioCardDTO> cards = new ArrayList<>();
        if (items == null) return cards;
        for (CartItemDTO item : items) {
            for (int i = 0; i < item.getQuantity(); i++) {
                cards.add(item.getCard());
            }
        }
        return cards;
    }

    // Setter de compatibilité pour StoreClientService.clearShoppingCart()
    public void setCards(List<YughioCardDTO> ignored) {
        if (this.items == null) this.items = new ArrayList<>();
        else this.items.clear();
    }
}