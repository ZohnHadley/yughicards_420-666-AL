package com.cal.yughistore.model.user;

import com.cal.yughistore.model.yughiocard.YughioCard;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference("user-shopping-cart")
    private ApplicationUser applicationUser;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("shopping-cart-items")
    private List<CartItem> cartItemList = new ArrayList<>();

    @Builder
    public ShoppingCart(
            Long id,
            ApplicationUser applicationUser,
            List<CartItem> cartItemList)
    {
        this.id = id;
        this.applicationUser = applicationUser;
        setCartItemList(cartItemList);
    }


    // ── Helpers ───────────────────────────────────────────────────────────
    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList.clear();

        if (cartItemList != null) {
            for (CartItem item : cartItemList) {
                addCartItem(item);
            }
        }
    }

    public void addCartItem(CartItem cartItem) {
        if (cartItem == null) {
            return;
        }
        cartItem.setShoppingCart(this);
        this.cartItemList.add(cartItem);
    }

    public void removeCartItem(CartItem cartItem) {
        if (cartItem == null) {
            return;
        }
        this.cartItemList.remove(cartItem);
        cartItem.setShoppingCart(null);
    }

    public void addCard(YughioCard card, int qty) {
        for (CartItem item : getCartItemList()) {
            if (item.getCard().getId().equals(card.getId())) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }

        addCartItem(CartItem.builder()
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
                    removeCartItem(item);
                }
                return;
            }
        }
    }

    public ShoppingCart clearItems() {
        for (CartItem item : new ArrayList<>(this.cartItemList)) {
            removeCartItem(item);
        }
        return this;
    }
}