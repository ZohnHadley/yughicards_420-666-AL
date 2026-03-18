package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.user.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByApplicationUser_Id(Long id);
    ShoppingCart findByApplicationUser_Credentials_Email(String email);

    @Query("SELECT c FROM ShoppingCart c JOIN FETCH c.items i JOIN FETCH i.card WHERE c.applicationUser.id = :userId")
    ShoppingCart findByUserIdWithItems(@Param("userId") Long userId);

}