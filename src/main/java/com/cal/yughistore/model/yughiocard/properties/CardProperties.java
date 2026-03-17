package com.cal.yughistore.model.yughiocard.properties;

import com.cal.yughistore.model.TextImbbededObject;
import com.cal.yughistore.model.yughiocard.YughioCard;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "yughioCard")
public class CardProperties implements TextImbbededObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonBackReference("card-properties")
    private YughioCard yughioCard;

    @Override
    public String toEmbeddingText() {
        return String.format("CardProperties[id=%d]", id);
    }

}
