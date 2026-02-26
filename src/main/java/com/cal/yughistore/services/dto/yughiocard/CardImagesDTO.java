package com.cal.yughistore.services.dto.yughiocard;

import com.cal.yughistore.model.yughiocard.CardImages;
import com.fasterxml.jackson.databind.JsonNode;
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
    private Integer image_group_api_id;
    private String image_url;
    private String image_url_small;
    private String image_url_cropped;



    public static CardImagesDTO of(JsonNode jsonNode) {
        return CardImagesDTO.builder()
                .image_group_api_id(jsonNode.get("id").asInt())
                .image_url(jsonNode.get("image_url").asText())
                .image_url_small(jsonNode.get("image_url_small").asText())
                .image_url_cropped(jsonNode.get("image_url_cropped").asText())
                .build();
    }

    public static CardImagesDTO of(CardImages cardImages) {
        return CardImagesDTO.builder()
                .id(cardImages.getId())
                .image_group_api_id(cardImages.getImageGroupApiId())
                .image_url(cardImages.getImageUrl())
                .image_url_small(cardImages.getImageUrlSmall())
                .image_url_cropped(cardImages.getImageUrlCropped())
                .build();
    }

    public CardImages toCardImages() {
        return CardImages.builder()
                .id(this.id)
                .imageGroupApiId(this.image_group_api_id)
                .imageUrl(this.image_url)
                .imageUrlSmall(this.image_url_small)
                .imageUrlCropped(this.image_url_cropped)
                .build();
    }
}
