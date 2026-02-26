package com.cal.yughistore.model.yughiocard;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "yughioCard")
public class CardImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonBackReference
    private Long id;

    private int imageGroupApiId;
    private String imageUrl;
    private String imageUrlSmall;
    private String imageUrlCropped;

    @ManyToOne
    @JsonBackReference
    private YughioCard yughioCard;

    @Builder
    public CardImages(
            int imageGroupApiId,
            String imageUrl,
            String imageUrlSmall,
            String imageUrlCropped
    ) {
        this.imageGroupApiId = imageGroupApiId;
        this.imageUrl = imageUrl;
        this.imageUrlSmall = imageUrlSmall;
        this.imageUrlCropped = imageUrlCropped;
    }

}
