// service/order/OrderService.java
package com.cal.yughistore.service.user.Order;


import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.CartItem;
import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.order.Order;
import com.cal.yughistore.model.user.order.OrderItem;
import com.cal.yughistore.repository.user.order.OrderRepository;
import com.cal.yughistore.service.dto.user.order.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final BigDecimal USD_TO_CAD = new BigDecimal("1.36");

    private final OrderRepository orderRepository;

    // ── Crée une commande à partir du panier courant ───────────────────────
    @Transactional
    public Order createFromCart(ApplicationUser user,
                                ShoppingCart cart,
                                String shippingMethod) {

        BigDecimal shippingCost = "ship".equals(shippingMethod)
                ? new BigDecimal("3.99")
                : BigDecimal.ZERO;

        // Construit les OrderItems depuis les CartItems
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> buildOrderItem(cartItem))
                .collect(Collectors.toList());

        // Calcule le total (prix cartes + livraison)
        BigDecimal subtotal = orderItems.stream()
                .map(oi -> oi.getPriceAtPurchase()
                        .multiply(BigDecimal.valueOf(oi.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = subtotal.add(shippingCost).setScale(2, RoundingMode.HALF_UP);

        Order order = Order.builder()
                .applicationUser(user)
                .shippingMethod(shippingMethod)
                .totalPrice(total)
                .build();

        // Lie chaque item à la commande
        for (OrderItem item : orderItems) {
            item.setOrder(order);
            order.getItems().add(item);
        }

        return orderRepository.save(order);
    }

    // ── Liste les commandes d'un utilisateur ──────────────────────────────
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(OrderDTO::from)
                .collect(Collectors.toList());
    }

    // ── Détail d'une commande (vérifie l'appartenance) ────────────────────
    @Transactional(readOnly = true)
    public OrderDTO getOrderDetail(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException(
                        "Commande #" + orderId + " introuvable pour cet utilisateur."));
        return OrderDTO.from(order);
    }

    // ── Helper : construit un OrderItem depuis un CartItem ────────────────
    private OrderItem buildOrderItem(CartItem cartItem) {
        // Récupère le prix cardmarket (USD) et convertit en CAD
        BigDecimal usdPrice = BigDecimal.ZERO;
        if (cartItem.getCard().getCard_prices() != null
                && !cartItem.getCard().getCard_prices().isEmpty()) {
            String raw = cartItem.getCard().getCard_prices().get(0).getCardmarket_price();
            if (raw != null && !raw.isBlank()) {
                try { usdPrice = new BigDecimal(raw); } catch (NumberFormatException ignored) {}
            }
        }

        BigDecimal cadPrice = usdPrice.compareTo(BigDecimal.ZERO) > 0
                ? usdPrice.multiply(USD_TO_CAD).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return OrderItem.builder()
                .card(cartItem.getCard())
                .quantity(cartItem.getQuantity())
                .priceAtPurchase(cadPrice)
                .build();
    }
}