package com.cal.yughistore.model.user;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_user_id", nullable = false, unique = true)
    @JsonBackReference
    private ApplicationUser applicationUser;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shopping_cart_id") // FK in yughio_card, controlled by ShoppingCart
    @JsonBackReference
    private List<CartItem> cartItemList = new ArrayList<>();

    @Builder
    public ShoppingCart(
            Long id,
            ApplicationUser applicationUser,
            List<CartItem> cartItemList)
    {
        this.id = id;
        this.applicationUser = applicationUser;
        this.cartItemList = cartItemList;
    }


    // ── Helpers ───────────────────────────────────────────────────────────
    public void addCard(YughioCard card, int qty) {
        for (CartItem item : getCartItemList()) {
            if (item.getCard().getId().equals(card.getId())) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        getCartItemList().add(CartItem.builder()
                .shoppingCart(this)
                .card(card)
                .quantity(qty)
                .build());
    }

    public void removeOneCard(Long cardId) {
        for (int i = 0; i < getCartItemList().size(); i++) {
            CartItem item = getCartItemList().get(i);
            if (item.getCard().getId().equals(cardId)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    getCartItemList().remove(i);
                }
                return;
            }
        }
    }

    public ShoppingCart clearItems() {
        this.getCartItemList().clear();
        return this;
    }
}