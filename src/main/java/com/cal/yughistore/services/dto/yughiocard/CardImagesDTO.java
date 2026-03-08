package com.cal.yughistore.services.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardImages;
import com.cal.yughistore.model.yughiocard.CardPrices;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CardImagesDTO {

    private Long id;
    private int image_group_api_id;
    private String image_url;
    private String image_url_small;
    private String image_url_cropped;


    public static CardImagesDTO fromCardPrices(CardImages cardImages) {
        return CardImagesDTO.builder()
                .id(cardImages.getId())
                .image_group_api_id(cardImages.getImage_group_api_id())
                .image_url(cardImages.getImage_url())
                .image_url_small(cardImages.getImage_url_small())
                .image_url_cropped(cardImages.getImage_url_cropped())
                .build();
    }

    public CardImages toCardImages() {
        return new CardImages(

        );
    }

}
