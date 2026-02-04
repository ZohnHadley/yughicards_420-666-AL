package com.cal.yughistore.model;

import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
@Setter
@Table
public abstract class YughioCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long api_id;
    private String name = "no_name";
    private EnumCardType type = EnumCardType.NULL;
    private EnumFrameType frameType = EnumFrameType.NULL;
    private String description = "...";
    private String ygoprodeck_url = "";

    public  YughioCard(Long id, Long api_id, String name, EnumCardType type, EnumFrameType frameType, String description, String ygoprodeck_url){
        this.id = id;
        this.api_id = api_id;
        this.name = name;
        this.type = type;
        this.frameType = frameType;
        this.description = description;
        this.ygoprodeck_url = ygoprodeck_url;
    }
}
