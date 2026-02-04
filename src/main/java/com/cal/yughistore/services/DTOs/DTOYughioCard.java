package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public abstract class DTOYughioCard {
    private Long id;
    private Long api_id;
    private String name;
    private EnumCardType type;
    private EnumFrameType frameType;
    private String description;
    private String ygoprodeck_url;

}
