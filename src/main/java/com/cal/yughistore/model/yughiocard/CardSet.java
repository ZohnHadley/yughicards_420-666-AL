package com.cal.yughistore.model.yughiocard;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardSet {

    private String set_name;
    @Id
    @Column(unique = true)
    private String set_code;
    private String set_rarity;
    private String set_rarity_code;
    private String set_price;

    @ManyToOne
    @JoinColumn(name = "yughio_card_id")
    @JsonBackReference
    private YughioCard yughioCard;
}
