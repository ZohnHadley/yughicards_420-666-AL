package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.user.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}