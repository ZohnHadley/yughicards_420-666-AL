package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.cal.yughistore.service.dto.yughiocard.YughioCardDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ShoppingCartDTO {
    private Long id;
    @NotBlank
    private UserPublicDTO applicationUser;
    @Builder.Default
    private List<YughioCardDTO> cards = new ArrayList<>();


    public static ShoppingCartDTO of(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new IllegalArgumentException("Shopping cart must not be null");
        }
        if (shoppingCart.getCardList() == null) {
            throw new IllegalArgumentException("Shopping cart list cannot be null");
        }

        List<YughioCardDTO> cardList = new ArrayList<>();

        if (shoppingCart.getCardList() != null && !shoppingCart.getCardList().isEmpty()) {
            for (YughioCard card : shoppingCart.getCardList()) {
                cardList.add(YughioCardDTO.of(card));
            }
        }

        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .applicationUser(UserPublicDTO.of(shoppingCart.getApplicationUser()))
                .cards(cardList)
                .build();
    }

    public ShoppingCart toShoppingCart() {

        ApplicationUser userRef = null;
        if (this.applicationUser != null && this.applicationUser.getId() != null) {
            userRef = new ApplicationUser();
            userRef.setId(this.applicationUser.getId());
        }

        List<YughioCard> cardList = new ArrayList<>();

        if (this.getCards() != null) {
            for (YughioCardDTO card : this.getCards()) {
                cardList.add(card.toYughioCard());
            }
        }

        ShoppingCart cart = ShoppingCart.builder()
                .id(this.getId())
                .applicationUser(userRef)
                .cardList(cardList)
                .build();

        if (userRef != null) {
            userRef.setShoppingCart(cart);
        }

        return cart;
    }
}
