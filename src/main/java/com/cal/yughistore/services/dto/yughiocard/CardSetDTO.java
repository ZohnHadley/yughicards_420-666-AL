package com.cal.yughistore.services.dto.yughiocard;

public record CardSetDTO(
        String set_name,
        String set_code,
        String set_rarity,
        String set_rarity_code,
        String set_price
) {}