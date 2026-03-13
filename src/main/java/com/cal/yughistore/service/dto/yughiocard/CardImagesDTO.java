package com.cal.yughistore.service.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardImages;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CardImagesDTO {

    private int images_id;
    private String image_url;
    private String image_url_small;
    private String image_url_cropped;


    public static CardImagesDTO of(CardImages cardImages) {
        if (cardImages == null) {
            return new CardImagesDTO();
        }
        return CardImagesDTO.builder()
                .images_id(cardImages.getImages_id())
                .image_url(cardImages.getImage_url())
                .image_url_small(cardImages.getImage_url_small())
                .image_url_cropped(cardImages.getImage_url_cropped())
                .build();
    }

    public CardImages toCardImages() {
        return CardImages.builder()
                .images_id(getImages_id())
                .image_url(getImage_url())
                .image_url_small(getImage_url_small())
                .image_url_cropped(getImage_url_cropped())
                .build();
    }

}
