package com.cal.yughistore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
@Setter
public class CardImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int image_group_api_id;
    private String image_url;
    private String image_url_small;
    private String image_url_cropped;

    @ManyToOne
    private YughioCard yughioCard;

    @Builder
    public CardImages(
            int image_group_api_id,
            String image_url,
            String image_url_small,
            String image_url_cropped
    ) {
        this.image_group_api_id = image_group_api_id;
        this.image_url = image_url;
        this.image_url_small = image_url_small;
        this.image_url_cropped = image_url_cropped;
    }

}
