package com.cal.yughistore.model.user;

import com.cal.yughistore.model.yughiocard.YughioCard;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yughio_card_id", nullable = false)
    private YughioCard card;

    @Column(nullable = false)
    private int quantity = 1;
}