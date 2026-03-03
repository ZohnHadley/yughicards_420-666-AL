package com.cal.yughistore.repository;

import com.cal.yughistore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByApplicationUser_Id(Long id);
    ShoppingCart findByApplicationUser_Email(String id);

}