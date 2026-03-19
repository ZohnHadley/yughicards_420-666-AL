// model/order/OrderItem.java
package com.cal.yughistore.model.user.order;

import com.cal.yughistore.model.yughiocard.YughioCard;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private YughioCard card;

    @Column(nullable = false)
    private int quantity;

    // Prix snapshot au moment de l'achat (en CAD)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtPurchase;
}