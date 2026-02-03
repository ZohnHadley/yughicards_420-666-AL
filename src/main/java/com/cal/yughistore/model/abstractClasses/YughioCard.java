package com.cal.yughistore.model.abstractClasses;

import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Table(name = "yughio_cards")
public abstract class YughioCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long api_id;
    private String name;
    private EnumCardType type;
    private EnumFrameType frameType;
    private String description;
    private String ygoprodeck_url;
}
