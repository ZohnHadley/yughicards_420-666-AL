package com.cal.yughistore.services.DTOs;

import com.cal.yughistore.model.YughioCard;
import com.cal.yughistore.model.enums.EnumCardType;
import com.cal.yughistore.model.enums.EnumFrameType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class DTOYughioCard {
    private Long id;
    private Long apiID;
    private String name;
    private EnumCardType type;
    private EnumFrameType frameType;
    private String description;
    private String ygoprodeck_url;

}
