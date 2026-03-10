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
    @JsonBackReference
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_user_id", nullable = false, unique = true)
    @JsonBackReference
    private ApplicationUser applicationUser;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shopping_cart_id") // FK in yughio_card, controlled by ShoppingCart
    @JsonBackReference
    private List<YughioCard> cardList = new ArrayList<>();

    @Builder
    public ShoppingCart(
            Long id,
            ApplicationUser applicationUser,
            List<YughioCard> cardList)
    {
        this.id = id;
        this.applicationUser = applicationUser;
        this.cardList = cardList;
    }
}