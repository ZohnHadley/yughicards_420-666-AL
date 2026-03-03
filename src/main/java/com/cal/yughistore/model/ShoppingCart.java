package com.cal.yughistore.model;

import com.cal.yughistore.model.applicaitonuser.ApplicationUser;
import com.cal.yughistore.model.applicaitonuser.ClientUser;
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

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "shoppingCart")
    @JsonBackReference
    private ApplicationUser applicationUser;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shoppingCart")
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
