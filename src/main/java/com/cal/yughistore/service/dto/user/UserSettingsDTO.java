package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.UserSettings;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettingsDTO {

    private String language;

    public static UserSettingsDTO fromEntity(UserSettings userSettings) {
        return UserSettingsDTO.builder()
                .language(userSettings.getLanguage())
                .build();
    }
}