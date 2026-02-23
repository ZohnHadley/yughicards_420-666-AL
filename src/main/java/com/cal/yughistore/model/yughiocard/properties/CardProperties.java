package com.cal.yughistore.model.yughiocard.properties;

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
@ToString
@Table(name = "card_properties")
public class CardProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonBackReference
    private Long id;

    @OneToOne
    @JsonBackReference
    private YughioCard yughioCard;
}
