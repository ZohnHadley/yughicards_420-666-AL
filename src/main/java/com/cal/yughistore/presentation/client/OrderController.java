package com.cal.yughistore.presentation.client;

import com.cal.yughistore.service.dto.user.order.OrderDTO;
import com.cal.yughistore.service.user.ApplicationUserService;
import com.cal.yughistore.service.user.Order.OrderService;
import com.cal.yughistore.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;
    private final ApplicationUserService applicationUserService;

    // GET /api/v1/orders  → liste des commandes du user connecté
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getMyOrders(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    // GET /api/v1/orders/{id}  → détail d'une commande
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderDetail(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long userId = getCurrentUserId(request);
        return ResponseEntity.ok(orderService.getOrderDetail(id, userId));
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = JwtTokenUtils.getTokenFromRequest(request);
        return applicationUserService.getMe(token).getId();
    }
}