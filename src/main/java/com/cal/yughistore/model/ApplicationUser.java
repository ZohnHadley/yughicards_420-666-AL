package com.cal.yughistore.model;

import jakarta.persistence.*;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

}
