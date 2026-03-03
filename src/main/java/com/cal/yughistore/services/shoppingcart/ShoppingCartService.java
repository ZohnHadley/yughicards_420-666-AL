package com.cal.yughistore.services.shoppingcart;

import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.repository.ShoppingCartRepository;
import com.cal.yughistore.services.dto.shoppingcart.ShoppingCartDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(
            ShoppingCartService.class
    );
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public ShoppingCartDTO save(ShoppingCartDTO shoppingCartDTO) {
       try {
           ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCartDTO.toShoppingCart());
           ShoppingCartService.logger.info("Saved shopping cart: {}", savedShoppingCart);
           return ShoppingCartDTO.of(savedShoppingCart);
        } catch (Exception e) {
            logger.error("Error saving shopping cart: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserId(Long userId) {
        if(shoppingCartRepository.existsById(userId)){
            return null;
        }
        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Id(userId);
        logger.info("Shopping cart for user {}: {}", userId, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }

    @Transactional(readOnly = true)
    public ShoppingCartDTO getShoppingCartByUserEmail(String userEmail) {

        ShoppingCart shoppingCart = shoppingCartRepository.findByApplicationUser_Email(userEmail);
        logger.info("Shopping cart for user {}: {}", userEmail, shoppingCart);
        return ShoppingCartDTO.of(shoppingCart);
    }
}
