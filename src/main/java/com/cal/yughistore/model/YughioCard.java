package com.cal.yughistore.model;

import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public abstract class YughioCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long apiID;
    private String name;
    private EnumCardType type;
    private EnumFrameType frameType;
    private String description;
    private String ygoprodeck_url;
}
