package com.cal.yughistore.repository.user.order;

import com.cal.yughistore.model.user.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}