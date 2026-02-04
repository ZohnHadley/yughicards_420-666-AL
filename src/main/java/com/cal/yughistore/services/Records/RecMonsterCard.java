package com.cal.yughistore.services.Records;
import com.cal.yughistore.model.enums.EnumCardAttribute;
import com.cal.yughistore.model.enums.EnumFrameType;
import com.cal.yughistore.model.enums.EnumMonsterCardRace;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public record RecMonsterCard(Long api_id, String name, EnumFrameType frameType, String description, String ygoprodeck_url, int atk, int def, int level, EnumMonsterCardRace cardRace, EnumCardAttribute cardAttribute){
}
