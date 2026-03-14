package com.cal.yughistore.model.yughiocard;

import jakarta.persistence.*;
import lombok.*;


@Embeddable
@NoArgsConstructor
@Getter
@Builder
public class CardImages {
    private int images_id;
    private String image_url;
    private String image_url_small;
    private String image_url_cropped;

    @Builder
    public CardImages(
            int images_id,
            String image_url,
            String image_url_small,
            String image_url_cropped
    ) {
        this.images_id = images_id;
        this.image_url = image_url;
        this.image_url_small = image_url_small;
        this.image_url_cropped = image_url_cropped;
    }

}
