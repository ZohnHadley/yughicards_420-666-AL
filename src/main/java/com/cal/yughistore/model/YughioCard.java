package com.cal.yughistore.model;

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

    @ManyToOne
    private ShoppingCart shoppingCart = new ShoppingCart();

    public  YughioCard(Long id, Long apiID, String name, EnumCardType type, EnumFrameType frameType, String description, String ygoprodeck_url){
        this.id = id;
        this.apiID = apiID;
        this.name = name;
        this.type = type;
        this.frameType = frameType;
        this.description = description;
        this.ygoprodeck_url = ygoprodeck_url;
    }
}
