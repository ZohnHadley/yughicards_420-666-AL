package com.cal.yughistore.model.user;

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
    private ApplicationUser applicationUser;

    // ── Remplace l'ancien @OneToMany vers YughioCard ──────────────────────
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Builder
    public ShoppingCart(Long id, ApplicationUser applicationUser, List<CartItem> items) {
        this.id = id;
        this.applicationUser = applicationUser;
        this.items = items != null ? items : new ArrayList<>();
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    public void addCard(com.cal.yughistore.model.yughiocard.YughioCard card, int qty) {
        for (CartItem item : items) {
            if (item.getCard().getId().equals(card.getId())) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        items.add(CartItem.builder()
                .shoppingCart(this)
                .card(card)
                .quantity(qty)
                .build());
    }

    public void removeOneCard(Long cardId) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getCard().getId().equals(cardId)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    items.remove(i);
                }
                return;
            }
        }
    }

    public void clearItems() {
        items.clear();
    }
}