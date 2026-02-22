package com.cal.yughistore.services.DTOs.applicationuser;

import com.cal.yughistore.model.applicaitonuser.UserSettings;
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