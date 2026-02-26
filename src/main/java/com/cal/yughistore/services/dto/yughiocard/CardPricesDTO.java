package com.cal.yughistore.services.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardPrices;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CardPricesDTO {

    private Long id;
    private Double cardmarket_price;
    private Double tcgplayer_price;
    private Double ebay_price;
    private Double amazon_price;
    private Double coolstuffinc_price;

    public static CardPricesDTO of(JsonNode jsonNode) {
        return CardPricesDTO.builder()
                .cardmarket_price(jsonNode.get("cardmarket_price").asDouble())
                .tcgplayer_price(jsonNode.get("tcgplayer_price").asDouble())
                .ebay_price(jsonNode.get("ebay_price").asDouble())
                .amazon_price(jsonNode.get("amazon_price").asDouble())
                .coolstuffinc_price(jsonNode.get("coolstuffinc_price").asDouble())
                .build();
    }

    public static CardPricesDTO of(CardPrices cardPrices) {
        return CardPricesDTO.builder()
                .id(cardPrices.getId())
                .cardmarket_price(cardPrices.getCardmarketPrice())
                .tcgplayer_price(cardPrices.getTcgplayerPrice())
                .ebay_price(cardPrices.getEbayPrice())
                .amazon_price(cardPrices.getAmazonPrice())
                .coolstuffinc_price(cardPrices.getCoolstuffincPrice())
                .build();
    }

    public CardPrices toCardPrices() {
        return CardPrices.builder()
                .id(this.getId())
                .cardmarketPrice(this.getCardmarket_price())
                .tcgplayerPrice(this.getTcgplayer_price())
                .ebayPrice(this.getEbay_price())
                .amazonPrice(this.getAmazon_price())
                .coolstuffincPrice(this.getCoolstuffinc_price())
                .build();
    }

}
